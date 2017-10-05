package com.impaq.training.spring.webfluxexamples.ex02jdbc.reader.async;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.*;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.ex02jdbc.BillingRecord;

import io.reactivex.*;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class AsyncBillingController {

    private final AsyncBillingService asyncBillingService;
    private final AsyncBackpresureBillingService asyncBackpresureBillingService;

    @GetMapping(value = "/billingAsync/hello", produces = TEXT_PLAIN_VALUE)
    public Single<String> helloAsync(){
        return Single.just("Hello async controller, returned single").delay(5, SECONDS);
    }

    @GetMapping(value = "/billingAsync/controller", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Observable<BillingRecord> loadAllBillingRecord(){
        return asyncBillingService.findBillingRecords();
    }

    /*
    curl -i -N --request GET \
                                                    -H "accept: application/stream+json" \
                                                      --url http://localhost:8080/billingAsync/controllerBackpressure
     */
    @GetMapping(value = "/billingAsync/controllerBackpressure", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flowable<BillingRecord> loadAllBillingRecordBackpressure(){
        return asyncBackpresureBillingService.findBillingRecords();
    }


    @GetMapping(value = "/billingAsync/slowController", produces = APPLICATION_JSON_VALUE)
    public Observable<BillingRecord> loadAllBillingRecordSlowly(){
        /*@formatter:off*/
        return asyncBillingService
                .findBillingRecords()
                .take(5)
                .zipWith(Observable.interval(1, SECONDS), (billing, interval) -> billing);
        /*@formatter:on*/
    }

    @GetMapping(value = "/billingAsync/counter", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<CounterValue> counter(){
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(number -> Flux.just(number).delayElements(Duration.ofSeconds(number)))
                .map(number -> String.format("Event generated after %d s.\n", number))
                .map(CounterValue::new)
                .take(10);
    }

    @GetMapping(value = "/billingAsync/counterObservable", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Observable<CounterValue> counterObservable(){
        return Observable.interval(1, SECONDS)
                .flatMap(number -> Observable.just(number).delay(number, SECONDS))
                .map(number -> String.format("Event generated after %d s.\n", number))
                .map(CounterValue::new)
                .take(10);
    }

    @Value
    public static class CounterValue{
        private String value;
    }
}
