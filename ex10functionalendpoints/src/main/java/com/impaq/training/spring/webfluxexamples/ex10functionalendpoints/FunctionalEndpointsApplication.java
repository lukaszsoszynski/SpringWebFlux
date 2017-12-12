package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;

@SpringBootApplication
public class FunctionalEndpointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunctionalEndpointsApplication.class, args);
    }

    /*
    curl -i -XGET localhost:8010/ex10/billing/8

    curl -i -XPOST\
                                -H "Content-Type: application/json"\
                                -H "Accept: text/plain"\
                                -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}' \
                                 localhost:8010/ex10/billing

     curl -H "correlation-id: randomstring" -i -XHEAD localhost:8010/ex10/billing/59fdfa5036ab15267da2c070

     400 - error handler:
     curl -i -XGET localhost:8010/ex10/billing/incrT
     */
    @Bean
    public RouterFunction<?> routerFunction(BillingHandler h){
        return route(GET("/ex10/billing/{id}")
                    .and(accept(APPLICATION_JSON)),
                        h::findBilling)
                .andRoute(POST("/ex10/billing")
                    .and(contentType(APPLICATION_JSON)
                    .and(accept(TEXT_PLAIN))),
                        h::createBillingRecord)
                .andRoute(HEAD("/ex10/billing/{id}"),
                        h::checkIfExists);
    }

    //How to start server without spring boot?, annotation EnableWebFlux also needed
//    @Bean
//    public HttpServer httpServer(RouterFunction<?> routerFunction){
//        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
//        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
//        HttpServer httpServer = HttpServer.create(8010);
//        httpServer.newHandler(adapter);
//        return httpServer;
//    }

    @Bean
    public WebFilter correlationIdFilter(){
        return new CorrelationIdWebFilter();
    }

    @Bean
    public WebExceptionHandler statusCodeExceptionHandler(){
        return new StatusCodeExceptionHandler();
    }
}
