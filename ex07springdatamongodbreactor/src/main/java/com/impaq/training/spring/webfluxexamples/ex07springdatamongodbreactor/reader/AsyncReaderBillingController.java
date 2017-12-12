package com.impaq.training.spring.webfluxexamples.ex07springdatamongodbreactor.reader;

import static org.springframework.http.MediaType.*;

import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AsyncReaderBillingController {

    private final BillingService billingRepository;



    /*
    curl -i -N --request GET \
                                           --url http://localhost:8007/ex07/billing \
                                           --header 'accept: application/stream+json'

   ./massive.sh 5 "curl -i -N --request GET --url http://localhost:8007/ex07/billing --header 'accept: application/stream+json'"
     */
    @GetMapping(value = "/ex07/billing", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<BillingRecord> findAll(){
//        billingRepository.findAll().map(BillingRecord::toCsvString).subscribe(log::info);
//        return Flux.empty();
        return billingRepository.findAll();
    }

    @PutMapping(value = "/billing",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public BillingRecord update(BillingRecord record){
        return record;
    }

}
