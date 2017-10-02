package com.impaq.training.spring.webfluxexamples.ex00jdbc.datagenerator;

import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
@EnableTransactionManagement
public class Application {

    private final static int NUMBER_OF_RECORDS_TO_GENERATE = 1_000_000;
    public static final int BATCH_SIZE = 100;

    public /*static*/ void main(String[] args){
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        DataGeneratorService dataGeneratorService = applicationContext.getBean(DataGeneratorService.class);
        IntStream
                .range(0, NUMBER_OF_RECORDS_TO_GENERATE)
                .filter(i -> (i % BATCH_SIZE) == 0)
                .forEach(i -> dataGeneratorService.storeInDatabaseRandomRecords(BATCH_SIZE));
        applicationContext.close();
    }

    @Bean
    public DataGeneratorService dataGeneratorService(JdbcTemplate jdbcTemplate){
        return new DataGeneratorService(jdbcTemplate);
    }

}
