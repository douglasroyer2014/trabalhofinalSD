package edu.movietheater.seat.message;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.movietheater.seat.entity.Seat;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishMessage(Seat seat, String messageType) {
        CustomMessage message = new CustomMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        message.setIdEntity(seat.getId() == null ? null : seat.getId().toString());
        message.setObject(seat);
        message.setMessageType(messageType);

        template.convertAndSend(MQConfig.EXCHANGE,
                                MQConfig.ROUTING_KEY, message);
    }
}