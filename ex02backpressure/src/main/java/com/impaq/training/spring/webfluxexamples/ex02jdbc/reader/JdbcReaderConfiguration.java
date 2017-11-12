package com.impaq.training.spring.webfluxexamples.ex02jdbc.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcReaderConfiguration {

    public static final String SQL_QUERY = "select first_name, last_name, type, start_time, duration from billing_record";

    public static void main(String[] args){
        SpringApplication.run(JdbcReaderConfiguration.class, args);
    }


}
