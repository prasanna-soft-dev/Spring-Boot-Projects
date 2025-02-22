package com.devsync.DevSync.Service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "devsync-messages", groupId = "devsync-group")
    public void listenMessages(String message){
        System.out.println("Received Message: " + message);
    }
}
