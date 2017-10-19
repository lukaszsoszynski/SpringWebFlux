package com.impaq.training.spring.webfluxexamples.ex06springdatamongodb;

import org.springframework.data.repository.reactive.RxJava2CrudRepository;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

public interface BillingRepository extends RxJava2CrudRepository<BillingRecord,String> {

}
