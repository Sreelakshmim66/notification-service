package com.notification_server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class NotificationDtos {

    @Data
    public static class CreateNotificationRequest {
        @NotBlank
        private String itinNumber;

        @NotBlank
        private String userName;

        private String userEmail;

        @NotBlank
        private String message;
    }

    @Data
    public static class NotificationResponse {
        private String itinNumber;
        private String userName;
        private String userEmail;
        private String message;
        private String createdAt;
    }
}
