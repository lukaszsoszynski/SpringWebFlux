package com.impaq.training.spring.webfluxexamples.ex05mongodbreactivestreamsdriver;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    /*
    curl -i -N -H "accept: text/event-stream"  localhost:8080/ex05/billing
     */
    @GetMapping(path = "/ex05/billing", params = "slow!=true", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<BillingRecord> findAllBillingRecords(){
        return billingService.findAll();
    }

    /*
    curl -i -N -H "accept: application/stream+json"  "localhost:8080/ex05/billing?slow=true"
     */
    @GetMapping(path = "/ex05/billing", params = "slow=true", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<BillingRecord> findAllBillingRecordsSlow(){
        return billingService.findAllSlow();
    }

}
