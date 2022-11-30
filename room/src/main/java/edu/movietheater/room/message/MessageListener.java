package edu.movietheater.room.message;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.movietheater.room.entity.Room;
import edu.movietheater.room.entity.Seat;
import edu.movietheater.room.repository.RoomRepository;

@Service
public class MessageListener {

    @Autowired
    RoomRepository repository;

    @Transactional
    @RabbitListener(queues = MQConfig.SEAT)
    public void listener(CustomMessage message) {
        String idRoom = (String)((LinkedHashMap) message.getObject()).get("idRoom");
        Optional<Room> room = this.repository.findById(UUID.fromString(idRoom));
        switch (message.getMessageType()) {
            case "addSeatRoom":
                this.repository.updateCapacityById(room.get().getCapacity() + 1, room.get().getId());
                break;
            case "removeSeat":
                this.repository.updateCapacityById(room.get().getCapacity() - 1, room.get().getId());
                break;
            default:
                System.out.println(message.getMessageType());
                break;
        }
    }
}
