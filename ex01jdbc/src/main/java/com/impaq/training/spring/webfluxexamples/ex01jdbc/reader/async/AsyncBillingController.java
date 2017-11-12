package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AsyncBillingController {

    private final JsonFactory jsonFactory;
    private final AsyncBillingService asyncBillingService;

    /*
    curl -i -N http://localhost:8001/ex01/billing/semiasync/records
     */
    @SneakyThrows
    @GetMapping(path = "/ex01/billing/semiasync/records", produces = APPLICATION_JSON_VALUE)
    public void loadBilingRecords(Writer writer){
        writeJsonArray(writer, this::loadAndWriteBillingRecords);
    }

    /*
    curl -i -N http://localhost:8001/ex01/billing/async/records

     */
    @SneakyThrows
    @GetMapping(path = "/ex01/billing/async/records", produces = APPLICATION_JSON_VALUE)
    public void loadBilingRecordsInParallel(Writer writer){
        writeJsonArray(writer, this::loadAndWriteBillingRecordsInParallel);
    }

    private void writeJsonArray(Writer writer, Consumer<JsonGenerator> jsonGeneratorConsumer) throws IOException {
        try(JsonGenerator generator = jsonFactory.createGenerator(writer)) {
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            generator.setCodec(new ObjectMapper());
            generator.writeStartArray();

            //loading records from database
            jsonGeneratorConsumer.accept(generator);

            generator.writeEndArray();
        }
    }

    private void loadAndWriteBillingRecords(JsonGenerator generator) {
        asyncBillingService
                .findBillingRecords()
                .forEach(billingRecord -> {
//                    TimeUnit.MILLISECONDS.sleep(100);
                    generator.writeObject(billingRecord);
                });
    }

    private void loadAndWriteBillingRecordsInParallel(JsonGenerator generator){
        asyncBillingService
                .findBillingRecords()
                .subscribeOn(Schedulers.io())
                .blockingForEach(billingRecord -> {
//                    log.info("Sending billing record {} through the wire.", billingRecord);
//                    TimeUnit.MILLISECONDS.sleep(100);
                    generator.writeObject(billingRecord);
                });
    }
}
