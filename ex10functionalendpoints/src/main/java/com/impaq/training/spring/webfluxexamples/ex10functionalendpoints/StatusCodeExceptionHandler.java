package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import static org.springframework.http.MediaType.TEXT_PLAIN;

import java.util.Objects;

import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import reactor.core.publisher.Mono;

public class StatusCodeExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if(ex instanceof StatusCode){
            return handleException(exchange, (StatusCode) ex, ex.getMessage());
        }
        return Mono.error(ex);
    }

    private Mono<Void> handleException(ServerWebExchange exchange, StatusCode statusCode, String message) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode.getStatusCode());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(TEXT_PLAIN);
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        Mono<DataBuffer> content = Mono.just(message)
                .filter(Objects::nonNull)
                .filter(text -> text.length() > 0)
                .map(String::getBytes)
                .map(bytes -> dataBufferFactory.wrap(bytes));
        return response.writeWith(content);
    }
}
