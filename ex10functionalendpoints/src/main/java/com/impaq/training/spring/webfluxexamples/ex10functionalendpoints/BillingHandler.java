package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BillingHandler {

    private final BillingService billingService;

    public Mono<ServerResponse> findBilling(ServerRequest serverRequest) {
        Mono<BillingRecord> billingRecordMono = billingService
                .findById(serverRequest.pathVariable("id"))
                .cache();//One database query
        return billingRecordMono
                .flatMap(record -> ServerResponse.ok().contentType(APPLICATION_JSON).body(billingRecordMono, BillingRecord.class))//record is ignored
                .switchIfEmpty(ServerResponse.notFound().build());//if mono is empty then 404 status code is returned
//        return Mono.just(serverRequest.pathVariable("id"))
//                .flatMap(billingId -> billingService.findById(billingId))
//                .compose(billingRecord -> ServerResponse.ok().contentType(APPLICATION_JSON).body(billingRecord, BillingRecord.class))
//                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createBillingRecord(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BillingRecord.class)
                .compose(billingRecordMono -> billingService.createBillingRecord(billingRecordMono))
                .compose(billingRecordIdMono -> ServerResponse.ok().contentType(APPLICATION_JSON).body(billingRecordIdMono, String.class));

    }
}
