package com.movietheater.ticket.message;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movietheater.ticket.repository.TicketRepository;

@Service
public class MessageListener {

    @Autowired
    TicketRepository repository;

    @Transactional
    @RabbitListener(queues = MQConfig.SESSION)
    public void listenerSession(CustomMessage message){
        switch (message.getMessageType()) {
            case "removeSession":
                this.repository.deleteByIdSession(UUID.fromString(message.getIdEntity()));
                break;
            case "removeSeat":
                this.repository.deleteByIdSeat(UUID.fromString(message.getIdEntity()));
                break;
            default:
                System.out.println(message.getMessageType());
                break;
        }
    }
}
