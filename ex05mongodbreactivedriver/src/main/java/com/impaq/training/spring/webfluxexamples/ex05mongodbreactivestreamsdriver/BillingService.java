package com.impaq.training.spring.webfluxexamples.ex05mongodbreactivestreamsdriver;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BillingService {

    @Delegate
    private final BillingRepository billingRepository;

    public Flux<BillingRecord> findAllSlow() {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(500));
        return billingRepository
                .findAll()
                .zipWith(intervalFlux, (billingRecord, counter) -> billingRecord);
    }
}
