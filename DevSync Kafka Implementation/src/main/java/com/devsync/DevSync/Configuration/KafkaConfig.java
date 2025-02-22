package com.devsync.DevSync.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic messageTopic(){
        return new NewTopic("devsync-messages", 1, (short) 1);
    }
}
