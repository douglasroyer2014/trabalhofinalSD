package com.movie.movie.message;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.movie.entity.Movie;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishMessage(Movie movie, String messageType) {
        CustomMessage message = new CustomMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        message.setIdEntity(movie.getId().toString());
        message.setObject(movie);
        message.setMessageType(messageType);
        template.convertAndSend(MQConfig.EXCHANGE,
                                MQConfig.ROUTING_KEY, message);
    }
}
