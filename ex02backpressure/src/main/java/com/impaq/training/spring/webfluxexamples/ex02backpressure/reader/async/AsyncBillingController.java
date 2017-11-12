package com.impaq.training.spring.webfluxexamples.ex02backpressure.reader.async;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.*;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class AsyncBillingController {

    private final AsyncBillingService asyncBillingService;
    private final AsyncBackpresureBillingService asyncBackpresureBillingService;

    /*
    curl -i -N -H "accept: text/plain" http://localhost:8002/ex02/billingAsync/hello
     */
    @GetMapping(value = "/ex02/billingAsync/hello", produces = TEXT_PLAIN_VALUE)
    public Single<String> helloAsync(){
        return Single.just("Hello async controller, returned single").delay(5, SECONDS);
    }

    /* Works from time to time with -Xmx32M
    - application/json
        curl -i -N -H "accept: application/json" http://localhost:8002/ex02/billingSemiAsync/controller
    - application/stream+json
        curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingSemiAsync/controller
    - text/event-stream
        curl -i -N -H "accept: text/event-stream" http://localhost:8002/ex02/billingSemiAsync/controller
     */
    @GetMapping(value = "/ex02/billingSemiAsync/controller", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Observable<BillingRecord> loadAllBillingRecord(){
        return asyncBillingService
                .findBillingRecords()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /* Works from time to time with -Xmx32M
    - application/json
        curl -i -N -H "accept: application/json" http://localhost:8002/ex02/billingAsync/controllerBackpressure
    - application/stream+json
        curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingAsync/controllerBackpressure
    - text/event-stream
        curl -i -N -H "accept: text/event-stream" http://localhost:8002/ex02/billingAsync/controllerBackpressure

    Massive:
        ./massive.sh 10 'curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingAsync/controllerBackpressure'
        ./massive.sh 10 'curl -i -N -H "accept: text/event-stream" http://localhost:8002/ex02/billingAsync/controllerBackpressure'
     */
    @GetMapping(value = "/ex02/billingAsync/controllerBackpressure", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flowable<BillingRecord> loadAllBillingRecordBackpressure(){
        return asyncBackpresureBillingService.findBillingRecords()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }


    /*
    Whole method content is executed in Reactor threads!!!

    curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingAsync/slowController

    ./massive.sh 10 'curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingAsync/slowController'
     */
    @GetMapping(value = "/ex02/billingAsync/slowController", produces = {APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Observable<BillingRecord> loadAllBillingRecordSlowly(){
        /*@formatter:off*/
        return asyncBillingService
                .findBillingRecords()
                .take(5)
                .zipWith(Observable.interval(1, SECONDS), (billing, interval) -> billing);
        /*@formatter:on*/
    }

    /*
    curl -i -N -H "accept: application/stream+json" http://localhost:8002/ex02/billingAsync/counter
    curl -i -N -H "accept: application/json" http://localhost:8002/ex02/billingAsync/counter

     */
    @GetMapping(value = "/ex02/billingAsync/counter", produces = {APPLICATION_JSON_VALUE, APPLICATION_STREAM_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
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
