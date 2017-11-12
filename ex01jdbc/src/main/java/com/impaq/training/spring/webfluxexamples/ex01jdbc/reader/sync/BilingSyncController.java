package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.Writer;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
public class BilingSyncController {
    private final BillingService billingService;

    /*
    //256
    curl -i http://localhost:8001/ex01/1/billing/sync/records
     */
    @GetMapping(path = "/ex01/1/billing/sync/records", produces = APPLICATION_JSON_VALUE)
    public List<BillingRecord> getBillingRecords(){
        return billingService.findBillingRecords();
    }

    /*
    curl -i http://localhost:8001/ex01/2/billing/sync/records
     */
    @GetMapping(path = "/ex01/2/billing/sync/records", produces = APPLICATION_JSON_VALUE)
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
