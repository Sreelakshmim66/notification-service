package com.notification_server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 2000)
    private String message;

    /** Raw client IP received from booking-service */
    private String clientIp;

    /** Country resolved from clientIp via ipapi.co — may be null if lookup fails */
    private String country;

    /** City resolved from clientIp — may be null */
    private String city;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
