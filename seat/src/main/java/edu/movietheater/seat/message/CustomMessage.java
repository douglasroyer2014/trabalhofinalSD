package edu.movietheater.seat.message;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomMessage {

    private String messageId;
    private Date messageDate;
    private String idEntity;
    private String messageType;
    private Object object;

}