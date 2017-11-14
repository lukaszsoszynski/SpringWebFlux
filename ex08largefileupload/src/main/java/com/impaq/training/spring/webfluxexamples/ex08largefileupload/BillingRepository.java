package com.impaq.training.spring.webfluxexamples.ex08largefileupload;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

public interface BillingRepository extends ReactiveSortingRepository<BillingRecord,String> {


}
