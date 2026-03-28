package com.notification_server.service;

import com.notification_server.dto.NotificationDtos;
import com.notification_server.entity.Notification;
import com.notification_server.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final IpGeoService ipGeoService;

    public NotificationDtos.NotificationResponse send(NotificationDtos.CreateNotificationRequest req) {
        // Resolve country + city from the client IP (non-blocking best-effort)
        String[] geo = ipGeoService.resolve(req.getClientIp());

        Notification notification = new Notification();
        notification.setUserId(req.getUserId());
        notification.setMessage(req.getMessage());
        notification.setClientIp(req.getClientIp());
        notification.setCountry(geo[0]);
        notification.setCity(geo[1]);

        Notification saved = notificationRepository.save(notification);

        log.info("[NOTIFICATION] userId={} country={} city={} message={}",
                saved.getUserId(), saved.getCountry(), saved.getCity(), saved.getMessage());

        return new NotificationDtos.NotificationResponse(saved);
    }

    public List<NotificationDtos.NotificationResponse> getByUser(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationDtos.NotificationResponse::new)
                .collect(Collectors.toList());
    }

    public NotificationDtos.NotificationResponse getById(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Notification not found"));
        return new NotificationDtos.NotificationResponse(n);
    }
}
