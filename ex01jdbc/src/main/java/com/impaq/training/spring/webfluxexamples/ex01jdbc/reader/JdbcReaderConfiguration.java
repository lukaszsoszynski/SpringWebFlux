package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async.AsyncBillingService;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async.AsyncBillingServlet;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync.BillingService;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync.BillingServlet;

@Configuration
public class JdbcReaderConfiguration {

    public static final String SQL_QUERY = "select first_name, last_name, type, start_time, duration from billing_record";

    public static void main(String[] args){
        SpringApplication.run(JdbcReaderConfiguration.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(BillingService billingService){
        BillingServlet billingServlet = new BillingServlet(billingService);
        return new ServletRegistrationBean(billingServlet, "/ex01/billingSync");
    }

    @Bean
    public ServletRegistrationBean asyncBillingServletRegistrationBean(AsyncBillingService asyncBillingService){
        AsyncBillingServlet asyncBillingServlet = new AsyncBillingServlet(asyncBillingService);
        return new ServletRegistrationBean(asyncBillingServlet, "/ex01/billingAsync");
    }

    @Bean
    public JsonFactory jsonFactory(){
        return new JsonFactory();
    }
}
