package com.impaq.training.spring.webfluxexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.impaq.training.spring.webfluxexamples.ex00jdbc.reader.JdbcReaderConfiguration;

@SpringBootApplication
@Import({JdbcReaderConfiguration.class})
public class WebFluxExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxExamplesApplication.class, args);
	}
}
