package com.impaq.training.spring.webfluxexamples.ex02jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.impaq.training.spring.webfluxexamples.ex02jdbc.reader.JdbcReaderConfiguration;

@SpringBootApplication
@Import({JdbcReaderConfiguration.class})
public class Billing02Application {

	public static void main(String[] args) {
		SpringApplication.run(Billing02Application.class, args);
	}
}
