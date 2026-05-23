package edu.eci.patricia.infrastructure.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceClient {

    private final RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    public void registerEventReminder(UUID userId, UUID eventId, LocalDateTime eventDate) {
        try {
            Map<String, Object> body = Map.of(
                    "userId", userId.toString(),
                    "eventId", eventId.toString(),
                    "eventDate", eventDate.toString()
            );
            restTemplate.postForEntity(
                    notificationServiceUrl + "/api/event-reminders",
                    body,
                    Void.class
            );
            log.info("Reminder registrado para usuario {} evento {}", userId, eventId);
        } catch (Exception ex) {
            log.warn("No se pudo registrar reminder: {}", ex.getMessage());
        }
    }
}