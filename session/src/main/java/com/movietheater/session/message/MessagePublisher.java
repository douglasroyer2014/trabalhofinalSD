package com.movietheater.session.message;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movietheater.session.entity.Session;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishMessage(Session session, String messageType) {
        CustomMessage message = new CustomMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setIdEntity(session.getId().toString());
        message.setMessageType(messageType);

        template.convertAndSend(MQConfig.EXCHANGE,
                                MQConfig.ROUTING_KEY, message);
    }
}