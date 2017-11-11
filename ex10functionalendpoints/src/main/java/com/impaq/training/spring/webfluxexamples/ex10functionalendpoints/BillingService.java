package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Mono<BillingRecord> findById(String id) {
        return repository.findById(id);
    }

    public Mono<String> createBillingRecord(Mono<BillingRecord> mono) {
        return repository.saveAll(mono)
                .elementAt(0)
                .map(BillingRecord::getId);
    }

    public Mono<Boolean> exists(String id) {
        return repository.existsById(id);
    }
}
