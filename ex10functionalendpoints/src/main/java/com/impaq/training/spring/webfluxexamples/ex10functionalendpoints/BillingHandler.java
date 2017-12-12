package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Optional;

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
        /*
        Mono<BillingRecord> billingRecordMono = billingService
                .findById(serverRequest.pathVariable("id"))
                .cache();//One database query
        return billingRecordMono
                .flatMap(record -> ServerResponse.ok().contentType(APPLICATION_JSON).body(billingRecordMono, BillingRecord.class))//record is ignored
                .switchIfEmpty(ServerResponse.notFound().build());//if mono is empty then 404 status code is returned
        */

        return billingService
                .findById(getBillingId(serverRequest))
                .flatMapMany(this::billingRecordFound, this::identityExceptionMapper, this::billingRecordNotFound)
                .elementAt(0);
    }

    public Mono<ServerResponse> createBillingRecord(ServerRequest serverRequest) {
        BillingRecord billingRecord = null;
        Mono<ServerResponse> response = ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(billingRecord), BillingRecord.class);

        return serverRequest.bodyToMono(BillingRecord.class)
                .compose(billingRecordMono -> billingService.createBillingRecord(billingRecordMono))
                .compose(billingRecordIdMono -> ServerResponse.ok().contentType(APPLICATION_JSON).body(billingRecordIdMono, String.class));

    }

    public Mono<ServerResponse> checkIfExists(ServerRequest serverRequest) {
        return billingService.exists(getBillingId(serverRequest))
                .flatMap(exists -> exists? ServerResponse.ok().build() : ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> billingRecordNotFound() {
        return ServerResponse.notFound().build();
    }

    private Mono<ServerResponse> identityExceptionMapper(Throwable throwable) {
        return Mono.error(throwable);
    }

    private Mono<ServerResponse> billingRecordFound(BillingRecord billingRecord) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(billingRecord), BillingRecord.class);
    }

    private String getBillingId(ServerRequest serverRequest) {
        return Optional.of(serverRequest)
                .map(request -> request.pathVariable("id"))
                .filter(this::validate)
                .orElseThrow(() -> new IdentifierValidationException(400, "Billing id not found or incorrect"));
    }

    private boolean validate(String id) {
        return !((id.length() == 5) && (isLowerCase(id.charAt(0))) && isUpperCase(id.charAt(4)));
    }
}
