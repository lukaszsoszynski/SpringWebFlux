package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync;

import java.io.Writer;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impaq.training.spring.webfluxexamples.ex01jdbc.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
public class BilingSyncController {

    private final BillingService billingService;

    @GetMapping(path = "/api/1/billing/sync/records", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BillingRecord> getBillingRecords(){
        return billingService.findBillingRecords();
    }

    @GetMapping(path = "/api/2/billing/sync/records", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public void writeBilingRecords(Writer writer){
        JsonFactory jsonFactory = new JsonFactory();
        try(JsonGenerator generator = jsonFactory.createGenerator(writer)) {
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            generator.setCodec(new ObjectMapper());
            generator.writeStartArray();
            billingService
                    .findBillingRecords()
                    .forEach(record -> writeObject(generator, record));
            generator.writeEndArray();
        }
    }

    @SneakyThrows
    private void writeObject(JsonGenerator generator, Object o){
        generator.writeObject(o);
    }
}
