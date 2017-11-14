package com.impaq.training.spring.webfluxexamples.ex06springdatamongodb;

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
    curl -i --request GET --url 'localhost:8006/ex06/billing' --header 'accept: application/stream+json'
     */
    @GetMapping(path = "/ex06/billing", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flowable<BillingRecord> findAllRevcord(){
        return billingService.findAll();
    }

}
