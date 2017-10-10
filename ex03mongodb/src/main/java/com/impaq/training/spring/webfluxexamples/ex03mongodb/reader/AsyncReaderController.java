package com.impaq.training.spring.webfluxexamples.ex03mongodb.reader;

import static org.springframework.http.MediaType.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.ex03mongodb.BillingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class AsyncReaderController {

    private final BillingRepository billingRepository;

    /*
    curl -i -N --request GET \
                                           --url http://localhost:8080/ex03/billing \
                                           --header 'content-type: application/stream+json'
     */
    @GetMapping(value = "/ex03/billing", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<BillingRecord> findAll(){
        return billingRepository.findAll();
    }

}
