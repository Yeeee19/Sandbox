package com.example.demo.controller;

import com.example.demo.entity.NotificationRecord;
import com.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/triggerInsertTestData")
    public ResponseEntity<String> triggerInsertTestData() {

        if (notificationService.isInsertTestDataRunning() || !notificationService.setInsertTestDataRunningFlag(true)) {
            return ResponseEntity.status(429).body("當前已有測試資料插入程序在執行中，請稍後再試。");
        }

        notificationService.triggerInsertTestData();
        return ResponseEntity.ok("開始插入測試資料。");
    }

    @PostMapping("/stopInsertTestData")
    public ResponseEntity<String> stopInsertTestData() {
        notificationService.setInsertTestDataRunningFlag(false);
        return ResponseEntity.ok("停止插入測試資料的請求已發出。");
    }

    @GetMapping("/polling")
    public ResponseEntity<List<NotificationRecord>> getNotificationsAfter(@RequestParam(value = "timestamp", required = false) Long timestamp) {
        return ResponseEntity.ok(notificationService.getNotificationsAfter(timestamp));
    }

    @GetMapping("/longPolling")
    public DeferredResult<NotificationRecord> subscribeLongPolling() {
        return notificationService.registerDeferredResult();
    }

    @GetMapping("/longPolling/mono")
    public Mono<ResponseEntity<NotificationRecord>> subscribeLongPollingMono() {
        return notificationService.registerMono()
                .timeout(Duration.ofSeconds(30))
                .map(ResponseEntity::ok)
                .onErrorResume(throwable ->
                        Mono.just(ResponseEntity.status(503).body(null))
                );
    }
}
