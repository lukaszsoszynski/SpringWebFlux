package com.impaq.training.spring.webfluxexamples.ex15security;

import static com.impaq.training.spring.webfluxexamples.ex15security.SecureApplication.BILLING_RESOURCE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.ex15security.model.BillingReference;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    /*
    Http security: Accessible by ROLE_USER
    
    200 With humble user credentials
        curl -i -XGET -u michal:password -H "Accept: application/json" localhost:8015/ex15/billing/59eccc4dcbcab52482278ac1

    403 With admin credentials
        curl -i -XGET -u lukasz:s3cret -H "Accept: application/json"  localhost:8015/ex15/billing/59eccc4dcbcab52482278ac2

    404 Billing with given id does not exists
        curl -i -XGET -u michal:password -H "Accept: application/json"  localhost:8015/ex15/billing/notvalidid

    401 Without credentials
        curl -i -XGET -H "Accept: application/json"  localhost:8015/ex15/billing/notvalidid
     */

    @GetMapping(path = BILLING_RESOURCE + "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Mono<BillingRecord>>> loadAllBillingRecord(@PathVariable("id") String id){
        return service.findById(id)
                .flatMap(record -> Mono.just(ResponseEntity.ok(Mono.just(record))))//we still have ability to response streaming
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /*
    Http security: Accessible by ROLE_ADMIN
    
    401 Without credentials
        curl -i -XPOST -H "content-type: application/json" localhost:8015/ex15/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'

    201 With admin credentials
        curl -i -XPOST -u 'lukasz:s3cret' -H "content-type: application/json" -H "accept: application/json"  localhost:8015/ex15/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'

    403 With humble user credentials
        curl -i -XPOST -u 'michal:password' -H "content-type: application/json" -H "accept: application/json"  localhost:8015/ex15/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'
     */
    @PostMapping(path = BILLING_RESOURCE, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<BillingReference> createBillingRecord(@RequestBody Mono<BillingRecord> mono){
        return service.createBillingRecord(mono);
    }

    /*
    Http security: authenticated additionally _method_security_
    
    401 Without credentials
        curl -i -XDELETE  -H "Accept: application/json" localhost:8015/ex15/billing/59fe1571dea6fb4208092ffb

    403 With humble user credentials
        curl -i -XDELETE -u michal:password -H "Accept: application/json" localhost:8015/ex15/billing/59fe1571dea6fb4208092ffb

    200 during business hours otherwise 403 (see WorkTimeService)
        curl -i -XDELETE -u lukasz:s3cret -H "Accept: application/json" localhost:8015/ex15/billing/59fe1571dea6fb4208092ffb

     */
    @DeleteMapping(path = BILLING_RESOURCE + "/{id}")
    public Mono<Void> deleteBilling(@PathVariable("id") String id){
        return service.delete(id);
    }
}
