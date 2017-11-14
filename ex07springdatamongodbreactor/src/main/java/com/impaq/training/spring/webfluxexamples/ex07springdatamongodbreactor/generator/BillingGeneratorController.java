package com.impaq.training.spring.webfluxexamples.ex07springdatamongodbreactor.generator;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingGeneratorController {

    private final BillingGeneratorService service;

    /*
    curl -i --request GET --url 'http://localhost:8007/ex07/generator/generate?count=150000'
     */
    @GetMapping(path = "/ex07/generator/generate")
    @ResponseStatus(CREATED)
    public Mono<BillingRecord> generateDocuments(@RequestParam("count") Integer count){
        return service.generateBillingRecord(count);

    }
}
