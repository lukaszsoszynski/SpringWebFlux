package com.impaq.training.spring.webfluxexamples.ex04mongodbasyncdriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;

@SpringBootApplication
@EnableConfigurationProperties(MongoProperties.class)
public class MongodbRxJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbRxJavaApplication.class, args);
    }

    @Bean
    public MongoClient mongoClient(MongoProperties properties){
        return MongoClients.create(properties.determineUri());
    }
}
