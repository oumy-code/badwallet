package com.ism.badwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BadwalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(BadwalletApplication.class, args);
    }

   
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}