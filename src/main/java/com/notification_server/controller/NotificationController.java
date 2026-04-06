package com.notification_server.controller;

import com.notification_server.dto.NotificationDtos;
import com.notification_server.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // POST /api/notifications  — also called directly by orchestrator if needed
    @PostMapping
    public ResponseEntity<NotificationDtos.NotificationResponse> send(
            @Valid @RequestBody NotificationDtos.CreateNotificationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.send(req));
    }
}
