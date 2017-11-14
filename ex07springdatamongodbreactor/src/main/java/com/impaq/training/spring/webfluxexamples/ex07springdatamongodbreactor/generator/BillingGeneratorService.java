package com.impaq.training.spring.webfluxexamples.ex07springdatamongodbreactor.generator;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.common.RandomRecord;
import com.impaq.training.spring.webfluxexamples.ex07springdatamongodbreactor.BillingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BillingGeneratorService {

    private final BillingRepository billingRepository;

    public Mono<BillingRecord> generateBillingRecord(Integer count) {
        return Flux.range(0, count)
                .map(index -> new RandomRecord())
                .cast(BillingRecord.class)
                .flatMap(record -> billingRepository.save(record).flatMapMany(value -> Flux.just(value)))
                .ignoreElements();
    }

}
