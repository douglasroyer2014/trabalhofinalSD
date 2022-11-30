package edu.movietheater.seat.message;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.movietheater.seat.repository.SeatRepository;

@Service
public class MessageListener {

    @Autowired
    SeatRepository repository;

    @Transactional
    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {
        switch (message.getMessageType()) {
            case "removeRoom":
                this.repository.deleteByIdRoom(UUID.fromString(message.getIdEntity()));
                break;
            default:
                System.out.println(message.getMessageType());
                break;
        }
    }
}