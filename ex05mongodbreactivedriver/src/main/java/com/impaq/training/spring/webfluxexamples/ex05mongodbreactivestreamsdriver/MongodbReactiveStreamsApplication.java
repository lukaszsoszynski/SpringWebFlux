package com.impaq.training.spring.webfluxexamples.ex05mongodbreactivestreamsdriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@SpringBootApplication
@EnableConfigurationProperties(MongoProperties.class)
public class MongodbReactiveStreamsApplication {

    @Bean
    public MongoClient mongoClient(MongoProperties properties){
        return MongoClients.create(properties.getUri());
    }

    public static void main(String[] args) {
        SpringApplication.run(MongodbReactiveStreamsApplication.class, args);
    }
}
