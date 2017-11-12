package com.impaq.training.spring.webfluxexamples.ex02backpressure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.impaq.training.spring.webfluxexamples.ex02backpressure.reader.JdbcReaderConfiguration;

@SpringBootApplication
@Import({JdbcReaderConfiguration.class})
public class Backpressure02Application {

	public static void main(String[] args) {
		SpringApplication.run(Backpressure02Application.class, args);
	}
}
