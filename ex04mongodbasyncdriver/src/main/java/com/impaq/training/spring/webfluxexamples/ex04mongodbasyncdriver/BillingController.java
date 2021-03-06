package com.impaq.training.spring.webfluxexamples.ex04mongodbasyncdriver;

import static org.springframework.http.MediaType.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    /*
    curl -i -N -H "accept: text/event-stream"  localhost:8004/ex04/billing

    ./massive.sh 3 'curl -i -N -H "accept: text/event-stream"  localhost:8004/ex04/billing'
     */
    @GetMapping(path = "/ex04/billing", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flowable<BillingRecord> loadBilling(){
        return Flowable.fromPublisher(billingService.findAll());
    }
}
