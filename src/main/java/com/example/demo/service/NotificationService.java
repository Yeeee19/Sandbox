package com.example.demo.service;

import com.example.demo.entity.NotificationRecord;
import com.example.demo.handler.NotificationWebSocketHandler;
import com.example.demo.repository.NotificationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private final NotificationRecordRepository notificationRecordRepository;

    private final AtomicBoolean insertTestDataRunning = new AtomicBoolean(false);

    private final String[] dummyMessages = {
            "新產品上線了！",
            "系統維護完成",
            "用戶數突破10萬！",
            "新功能發布預告",
            "伺服器狀態正常",
            "資料庫備份完成",
            "系統升級通知",
            "安全性更新",
            "效能優化完成",
            "新版本釋出",
            "促銷活動開始",
            "客服系統更新",
            "API 服務穩定",
            "監控警報解除",
            "備份系統檢查完成",
    };

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(NotificationRecordRepository notificationRecordRepository) {
        this.notificationRecordRepository = notificationRecordRepository;
    }

    @Async
    public void triggerInsertTestData() {
        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            if (!insertTestDataRunning.get()) {
                logger.info("插入測試資料的程序已停止。");
                break;
            }

            try {
                int sleepTime = 5000 + random.nextInt(5000); // 5 ~ 10 s
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setTimestamp(System.currentTimeMillis());
            notificationRecord.setMessage(dummyMessages[random.nextInt(dummyMessages.length)]);
            notificationRecordRepository.save(notificationRecord);

            // long polling
            notifyNewRecord(notificationRecord);

            // long polling (WebFlux)
            monoSink.tryEmitNext(notificationRecord);

            NotificationWebSocketHandler.broadcast(notificationRecord.getMessage());
        }

        setInsertTestDataRunningFlag(false);
    }

    public boolean setInsertTestDataRunningFlag(boolean running) {
        return insertTestDataRunning.compareAndSet(!running, running);
    }

    public boolean isInsertTestDataRunning() {
        return insertTestDataRunning.get();
    }



    // -------- polling --------
    public List<NotificationRecord> getNotificationsAfter(Long timestamp) {
        List<NotificationRecord> records;

        if (timestamp == null) {
            records = notificationRecordRepository.findAll();
        } else {
            records = notificationRecordRepository.findByTimestampGreaterThan(timestamp);
        }

        return records;
    }
    // -------- polling --------





    // -------- long polling --------
    private final List<DeferredResult<NotificationRecord>> deferredResults = Collections.synchronizedList(new ArrayList<>());

    public DeferredResult<NotificationRecord> registerDeferredResult() {
        DeferredResult<NotificationRecord> result = new DeferredResult<>(30 * 1000L);
        deferredResults.add(result);
        result.onCompletion(() -> deferredResults.remove(result));
        result.onTimeout(() -> deferredResults.remove(result));
        result.onError(throwable -> deferredResults.remove(result));
        return result;
    }

    public void notifyNewRecord(NotificationRecord record) {
        for (DeferredResult<NotificationRecord> result : deferredResults) {
            result.setResult(record);
        }
    }
    // -------- long polling --------




    // -------- long polling (WebFlux) --------
    private final Sinks.Many<NotificationRecord> monoSink = Sinks.many().multicast().onBackpressureBuffer();

    public Mono<NotificationRecord> registerMono() {
        return monoSink.asFlux().next();
    }
    // -------- long polling (WebFlux) --------




    // -------- websocket --------

    // -------- websocket --------
}
