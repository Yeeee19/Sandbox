package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NOTIFICATION_RECORD")
public class NotificationRecord {

    @Id
    @Column(name = "TIMESTAMP")
    private Long timestamp;

    @Column(name = "MESSAGE")
    private String message;
}
