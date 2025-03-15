package com.openai.code.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class CodeConfiguration {

    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) ->
        {
            request.getHeaders().add("Authorization" , "Bearer " + openAiKey);
            request.getHeaders().add("Content-Type", "application/json");

            return execution.execute(request,body);
        }));

        return restTemplate;

    }
}
