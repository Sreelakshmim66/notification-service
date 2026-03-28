package com.notification_server.dto;

import com.notification_server.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class NotificationDtos {

    @Data
    public static class CreateNotificationRequest {
        @NotBlank
        private String userId;

        @NotBlank
        private String message;

        private String clientIp;
    }

    @Data
    public static class NotificationResponse {
        private String id;
        private String userId;
        private String message;
        private String clientIp;
        private String country;
        private String city;
        private String createdAt;

        public NotificationResponse(Notification n) {
            this.id        = n.getId();
            this.userId    = n.getUserId();
            this.message   = n.getMessage();
            this.clientIp  = n.getClientIp();
            this.country   = n.getCountry();
            this.city      = n.getCity();
            this.createdAt = n.getCreatedAt() != null ? n.getCreatedAt().toString() : null;
        }
    }
}
