package com.movietheater.session.message;

import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movietheater.session.entity.Session;
import com.movietheater.session.repository.SessionRepository;

@Service
public class MessageListener {

    @Autowired
    SessionRepository repository;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {
        UUID id = UUID.fromString(message.getIdSession());
        Optional<Session> session = repository.findById(id);
        if (session.isPresent()) {
            this.repository.updateQntTicketById(session.get().getQntTicket() + 1, id);
        }
        System.out.println(message);
    }
}
