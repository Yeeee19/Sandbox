package com.example.demo.service;

import com.example.demo.entity.NotificationRecord;
import com.example.demo.repository.NotificationRecordRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class NotificationService {

    private final NotificationRecordRepository notificationRecordRepository;

    private volatile boolean insertTestDataRunning = false;

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

    public NotificationService(NotificationRecordRepository notificationRecordRepository) {
        this.notificationRecordRepository = notificationRecordRepository;
    }

    @Async
    public void triggerInsertTestData() {
        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            if (!insertTestDataRunning) {
                System.out.println("插入測試資料程序已停止。");
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
        }

        insertTestDataRunning = false;
    }

    public void setInsertTestDataRunningFlag(boolean running) {
        this.insertTestDataRunning = running;
    }

    public boolean isInsertTestDataRunning() {
        return insertTestDataRunning;
    }

    public List<NotificationRecord> getNotificationsAfter(Long timestamp) {
        List<NotificationRecord> records;

        if (timestamp == null) {
            records = notificationRecordRepository.findAll();
        } else {
            records = notificationRecordRepository.findByTimestampGreaterThan(timestamp);
        }

        return records;
    }
}
