package edu.movietheater.room.message;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.movietheater.room.entity.Room;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishMessage(Room room, String messageType) {
        CustomMessage message = new CustomMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        message.setIdEntity(room.getId().toString());
        message.setObject(room);
        message.setMessageType(messageType);
        template.convertAndSend(MQConfig.EXCHANGE,
                                MQConfig.ROUTING_KEY, message);
    }
}
