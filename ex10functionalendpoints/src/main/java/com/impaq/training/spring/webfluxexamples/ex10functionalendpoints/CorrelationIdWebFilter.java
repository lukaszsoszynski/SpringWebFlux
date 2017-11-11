package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import static java.util.Collections.singletonList;

import java.util.*;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.*;

import reactor.core.publisher.Mono;

class CorrelationIdWebFilter implements WebFilter {

    private static final String HEADER_NAME_CORRELATION_ID = "Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Optional.of(exchange)
                .map(ServerWebExchange::getRequest)
                .map(ServerHttpRequest::getHeaders)
                .map(httpHeaders -> httpHeaders.getOrDefault(HEADER_NAME_CORRELATION_ID, singletonList(UUID.randomUUID().toString())))
                .map(list -> list.get(0))
                .ifPresent(correlationId -> exchange
                        .getResponse()
                        .getHeaders()
                        .add(HEADER_NAME_CORRELATION_ID, correlationId));
        return chain.filter(exchange);
    }
}
