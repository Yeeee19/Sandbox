package com.example.demo.repository;

import com.example.demo.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {
    List<NotificationRecord> findByTimestampGreaterThan(Long timestamp);
}
