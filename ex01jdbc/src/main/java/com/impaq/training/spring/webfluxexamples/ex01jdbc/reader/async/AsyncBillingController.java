package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.Writer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
public class AsyncBillingController {

    private final AsyncBillingService asyncBillingService;

    /*
    curl -i http://localhost:8001/ex01/billing/async/records
     */
    @SneakyThrows
    @GetMapping(path = "/ex01/billing/async/records", produces = APPLICATION_JSON_VALUE)
    public void writeBilingRecords(Writer writer){
        JsonFactory jsonFactory = new JsonFactory();
        try(JsonGenerator generator = jsonFactory.createGenerator(writer)) {
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            generator.setCodec(new ObjectMapper());
            generator.writeStartArray();

            //loading records from database
            loadAndWriteBillingRecords(generator);

            generator.writeEndArray();
        }
    }

    private void loadAndWriteBillingRecords(JsonGenerator generator) {
        asyncBillingService
                .findBillingRecords()
                .forEach(billingRecord -> generator.writeObject(billingRecord));
    }

}
