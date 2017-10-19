package com.impaq.training.spring.webfluxexamples.ex03mongodb.reader;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.ex03mongodb.BillingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Flux<BillingRecord> findAll(){
        return repository
                .findAll()
                .map(record -> record.withId(null));
    }
}
