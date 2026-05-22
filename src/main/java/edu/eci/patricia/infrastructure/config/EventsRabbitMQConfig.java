package edu.eci.patricia.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EventsRabbitMQConfig {

    @Value("${rabbitmq.exchange.events}")
    private String eventsExchange;

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(eventsExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}