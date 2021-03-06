package com.impaq.training.spring.webfluxexamples.ex15security;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

public interface BillingRepository extends ReactiveCrudRepository<BillingRecord, String> {

}
