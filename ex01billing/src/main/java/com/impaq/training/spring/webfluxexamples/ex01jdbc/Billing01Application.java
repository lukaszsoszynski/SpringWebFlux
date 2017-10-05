package com.impaq.training.spring.webfluxexamples.ex01jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.JdbcReaderConfiguration;

@SpringBootApplication
@Import({JdbcReaderConfiguration.class})
public class Billing01Application {

	public static void main(String[] args) {
		SpringApplication.run(Billing01Application.class, args);
	}
}
