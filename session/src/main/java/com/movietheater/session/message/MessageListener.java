package com.movietheater.session.message;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movietheater.session.repository.SessionRepository;

@Service
public class MessageListener {

    @Autowired
    SessionRepository repository;

    @Transactional
    @RabbitListener(queues = MQConfig.MOVIE)
    @RabbitListener(queues = MQConfig.ROOM)
    public void listenerMovie(CustomMessage message) {
        switch (message.getMessageType()) {
            case "removeMovie":
                this.repository.deleteByIdMovie(UUID.fromString(message.getIdEntity()));
                break;
            case "removeRoom":
                this.repository.deleteByIdRoom(UUID.fromString(message.getIdEntity()));
                break;
            default:
                System.out.println(message.getMessageType());
                break;
        }
    }
}
