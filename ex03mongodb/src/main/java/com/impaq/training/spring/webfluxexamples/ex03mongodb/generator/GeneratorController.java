package com.impaq.training.spring.webfluxexamples.ex03mongodb.generator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.common.RandomRecord;
import com.impaq.training.spring.webfluxexamples.ex03mongodb.BillingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GeneratorController {

    private final BillingRepository billingRepository;

    @GetMapping(path = "/ex03/generator/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BillingRecord> generateDocuments(@RequestParam("count") Integer count){
        return Flux.range(0, count)
                .map(index -> new RandomRecord())
                .cast(BillingRecord.class)
                .flatMap(record -> billingRepository.save(record).flatMapMany(value -> Flux.just(value)))
                .ignoreElements();

    }
}
