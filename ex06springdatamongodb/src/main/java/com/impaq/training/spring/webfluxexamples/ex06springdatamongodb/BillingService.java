package com.impaq.training.spring.webfluxexamples.ex06springdatamongodb;

import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Flowable<BillingRecord> findAll(){
        return repository.findAll();
    }

}
