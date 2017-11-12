package com.impaq.training.spring.webfluxexamples.ex01jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.impaq.training.spring.webfluxexamples.ex01jdbc.datagenerator.DataGeneratorService;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.JdbcReaderConfiguration;

@SpringBootApplication
@EnableTransactionManagement
@Import({JdbcReaderConfiguration.class})
public class Jdbc01Application {

	public static void main(String[] args) {
		SpringApplication.run(Jdbc01Application.class, args);
	}

	@Bean
	public DataGeneratorService dataGeneratorService(JdbcTemplate jdbcTemplate){
		return new DataGeneratorService(jdbcTemplate);
	}
}
