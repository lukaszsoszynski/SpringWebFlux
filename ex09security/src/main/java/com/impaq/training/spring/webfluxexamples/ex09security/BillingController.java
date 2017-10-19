package com.impaq.training.spring.webfluxexamples.ex09security;

import static com.impaq.training.spring.webfluxexamples.ex09security.SecureApplication.BILLING_RESOURCE;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    /*
    curl -i -u lukasz:s3cret -H "accept: application/stream+json" localhost:8009/ex09/billing
    curl -i -H "accept: application/stream+json" localhost:8009/ex09/billing
     */
    @GetMapping(path = BILLING_RESOURCE, produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<BillingRecord> loadAllBillingRecord(){
        return service.findAll();
    }
}
