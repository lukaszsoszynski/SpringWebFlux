package com.impaq.training.spring.webfluxexamples.ex09security;

import static com.impaq.training.spring.webfluxexamples.ex09security.SecureApplication.BILLING_RESOURCE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.ex09security.model.BillingReference;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    /*
    200 With humble user credentials
        curl -i -XGET -u michal:password -H "Accept: application/json" localhost:8009/ex09/billing/59eccc4dcbcab52482278ac1

    403 With admin credentials
        curl -i -XGET -u lukasz:s3cret -H "Accept: application/json"  localhost:8009/ex09/billing/59eccc4dcbcab52482278ac2

    404 Billing with given id does not exists
        curl -i -XGET -u michal:password -H "Accept: application/json"  localhost:8009/ex09/billing/notvalidid

    401 Without credentials
        curl -i -XGET -H "Accept: application/json"  localhost:8009/ex09/billing/notvalidid
     */

    @GetMapping(path = BILLING_RESOURCE + "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Mono<BillingRecord>>> loadAllBillingRecord(@PathVariable("id") String id){
        return service.findById(id)
                .flatMap(record -> Mono.just(ResponseEntity.ok(Mono.just(record))))//we still have ability to response streaming
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /*
    401 Without credentials
        curl -i -XPOST -H "content-type: application/json" localhost:8009/ex09/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'

    201 With admin credentials
        curl -i -XPOST -u 'lukasz:s3cret' -H "content-type: application/json" -H "accept: application/json"  localhost:8009/ex09/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'

    403 With humble user credentials
        curl -i -XPOST -u 'michal:password' -H "content-type: application/json" -H "accept: application/json"  localhost:8009/ex09/billing -d '{"firstName":"Scott", "lastName":"Tiger", "duration":7}'
     */
    @PostMapping(path = BILLING_RESOURCE, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<BillingReference> createBillingRecord(@RequestBody Mono<BillingRecord> mono){
        return service.createBillingRecord(mono);
    }
}
