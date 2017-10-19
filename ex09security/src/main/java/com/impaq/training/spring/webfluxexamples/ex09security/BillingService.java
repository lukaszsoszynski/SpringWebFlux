package com.impaq.training.spring.webfluxexamples.ex09security;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Flux<BillingRecord> findAll(){
        return repository.findAll();
    }

}
