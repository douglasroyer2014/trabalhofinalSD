package com.movietheater.ticket.message;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movietheater.ticket.entity.Ticket;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishMessage(Ticket ticket) {
        CustomMessage message = new CustomMessage();
        message.setIdSession(ticket.getIdSession().toString());
        message.setMessage("addTicket");
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        template.convertAndSend(MQConfig.EXCHANGE,
                                MQConfig.ROUTING_KEY, message);
    }
}
