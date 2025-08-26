package com.example.demo.controller;

import com.example.demo.entity.NotificationRecord;
import com.example.demo.repository.NotificationRecordRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class NotificationController {

    private final NotificationRecordRepository notificationRecordRepository;

    public NotificationController(NotificationRecordRepository notificationRecordRepository) {
        this.notificationRecordRepository = notificationRecordRepository;
    }

    @PostMapping("/triggerInsertTestData")
    public String triggerInsertTestData() {
        Random random = new Random();

        for (int i = 0; i < 1; i++) {

            try {
                int sleepTime = 1000 + random.nextInt(2000); // 1000~3000 ms
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Thread was interrupted";
            }
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setTimestamp(System.currentTimeMillis());
            notificationRecordRepository.save(notificationRecord);
        }

        return "Test data insertion completed.";
    }

}
