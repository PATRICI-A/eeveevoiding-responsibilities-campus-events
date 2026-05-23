package edu.eci.patricia.infrastructure.messaging;

import edu.eci.patricia.infrastructure.messaging.dto.EventChangeEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventChangePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.events}")
    private String eventsExchange;

    @Value("${rabbitmq.routing-key.event-change}")
    private String eventChangeRoutingKey;

    public void publish(EventChangeEventDto event) {
        try {
            rabbitTemplate.convertAndSend(eventsExchange, eventChangeRoutingKey, event);
            log.info("Evento EVENT_CHANGE publicado para usuario {}", event.getTargetUserId());
        } catch (Exception ex) {
            log.warn("No se pudo publicar EVENT_CHANGE: {}", ex.getMessage());
        }
    }
}