package com.project.onlybuns.service; // ili .messaging ako si stavio u novi paket
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdMessageService {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AdMessageService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendAdPost(String username, String description, LocalDateTime createdAt) {
        Map<String, String> message = new HashMap<>();
        message.put("username", username);
        message.put("description", description);
        message.put("createdAt", createdAt.toString());

        amqpTemplate.convertAndSend("adsExchange", "", message); // isti naziv kao u RabbitMQConfig
    }
}
