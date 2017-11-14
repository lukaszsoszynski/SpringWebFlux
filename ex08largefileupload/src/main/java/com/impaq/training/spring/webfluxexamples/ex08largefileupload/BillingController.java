package com.impaq.training.spring.webfluxexamples.ex08largefileupload;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.net.URI;
import java.util.Optional;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.impaq.training.spring.webfluxexamples.common.RandomRecord;
import com.impaq.training.spring.webfluxexamples.ex08largefileupload.model.CloneRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private static final String PATH_BILLING = "/ex07/billing";
    private final BillingService billingService;

    /*
    curl -i -XPOST -H "Content-Type: application/stream+json" -d @billing.json localhost:8007/ex07/billing
     */
    @PostMapping(path = "/ex07/billing", consumes = APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> storeBillingRecords(@RequestBody Publisher<BillingRecord> billingRecordPublisher){
        return billingService.store(billingRecordPublisher);
    }

    /*
    curl -i localhost:8007/ex07/billing/count
     */
    @GetMapping(path = "/ex07/billing/count", produces = TEXT_PLAIN_VALUE)
    public Mono<String> getRecordCount(){
        return billingService.count();
    }

    /*
    curl -i -XGET -H "Accept: application/stream+json" "localhost:8007/ex07/billing?from=0&limit=1000000"
     */
    @GetMapping(path = PATH_BILLING, produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<BillingRecord> loadBillingRecord(@RequestParam(value = "from", required = false) Long from, @RequestParam(value = "limit", required = false) Long limit){
        return billingService.load(from, limit);
    }

    /*
    curl -i -XPATCH -H "content-type: application/json" localhost:8007/ex07/billing/insert -d '{"url":"http://localhost:8007/ex07/billing/random"}'
     */
    @PatchMapping(path = "/ex07/billing/insert", consumes = APPLICATION_JSON_VALUE)
    public Mono<Void> cloneBillingRecord(@RequestBody Mono<CloneRequest> cloneRequest){
        return cloneRequest.map(CloneRequest::getUrl)
                .map(URI::create)
                .flatMap(billingService::cloneBillingRecords);
    }

    /*
    curl -i -H "accept: application/stream+json" localhost:8007/ex07/billing/random
     */
    @GetMapping(path = PATH_BILLING + "/random", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<BillingRecord> generateRandomBillingRecord(@RequestParam(value = "max", required = false) Integer max){
        Integer count = Optional.ofNullable(max).orElse(100_000);
        return Flux.range(0, count)
                .map(r -> new RandomRecord());
    }
}
