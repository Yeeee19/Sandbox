package com.example.demo.controller;

import com.example.demo.entity.NotificationRecord;
import com.example.demo.repository.NotificationRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class NotificationController {

    private final NotificationRecordRepository notificationRecordRepository;

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

    public NotificationController(NotificationRecordRepository notificationRecordRepository) {
        this.notificationRecordRepository = notificationRecordRepository;
    }

    @PostMapping("/triggerInsertTestData")
    public ResponseEntity<String> triggerInsertTestData() {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {

            try {
                int sleepTime = 5000 + random.nextInt(5000); // 5 ~ 10 s
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(500).body("Thread was interrupted");
            }

            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setTimestamp(System.currentTimeMillis());
            notificationRecord.setMessage(dummyMessages[random.nextInt(dummyMessages.length)]);
            notificationRecordRepository.save(notificationRecord);
        }

        return ResponseEntity.ok("Test data insertion completed.");
    }

    @GetMapping("/notificationsAfter")
    public ResponseEntity<List<NotificationRecord>> getNotificationsAfter(@RequestParam(value = "timestamp", required = false) Long timestamp) {
        List<NotificationRecord> records;

        if (timestamp == null) {
            records = notificationRecordRepository.findAll();
        } else {
            records = notificationRecordRepository.findByTimestampGreaterThan(timestamp);
        }

        return ResponseEntity.ok(records);
    }
}
