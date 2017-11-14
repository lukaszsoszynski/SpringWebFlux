package com.impaq.training.spring.webfluxexamples.ex08largefileupload;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import java.net.URI;
import java.util.Optional;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;

    public Mono<Void> store(Publisher<BillingRecord> billingRecordPublisher) {
        return billingRepository.saveAll(billingRecordPublisher)
                .ignoreElements()
                .cast(Void.class);
    }

    public Mono<String> count() {
        return billingRepository.count().map(count -> count.toString());
    }

    public Flux<BillingRecord> load(Long from, Long limit) {
        //we load everything from mongodb...
        return billingRepository.findAll(/*new Sort(Sort.Direction.ASC, "lastName")*/)
                .skip(Optional.ofNullable(from).orElse(0L))
                .take(Optional.ofNullable(limit).orElse(Long.MAX_VALUE));
    }

    public Mono<Void> cloneBillingRecords(URI uri) {
        return WebClient
                .create()
                .get()
                .uri(uri)
                .accept(APPLICATION_STREAM_JSON)
                .exchange()//load documents from http server
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(BillingRecord.class))
                .map(billingRecord -> billingRecord.withId(null))
                .flatMap(billingRepository::save)//save documents to mongodb
                .ignoreElements()
                .cast(Void.class);
    }
}
