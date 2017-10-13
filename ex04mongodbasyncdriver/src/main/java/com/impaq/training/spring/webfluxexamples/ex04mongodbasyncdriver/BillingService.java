package com.impaq.training.spring.webfluxexamples.ex04mongodbasyncdriver;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import rx.RxReactiveStreams;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Publisher<BillingRecord> findAll(){
        return RxReactiveStreams.toPublisher(repository.findAll());
    }

}
