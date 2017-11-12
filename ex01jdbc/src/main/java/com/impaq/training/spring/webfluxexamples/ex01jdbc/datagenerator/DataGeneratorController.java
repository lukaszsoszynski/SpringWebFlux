package com.impaq.training.spring.webfluxexamples.ex01jdbc.datagenerator;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DataGeneratorController {

    public static final int BATCH_SIZE = 100;

    private final DataGeneratorService dataGeneratorService;

    /*
    curl -i -XPOST "http://localhost:8001/ex01/billing/?count=150000"
     */
    @PostMapping(path = "/ex01/billing", produces = TEXT_PLAIN_VALUE)
    public Callable<String> main(@RequestParam("count") Integer count){
        if((count % 100) != 0){
            return () -> String.format("Count must be multiplicity of %d", BATCH_SIZE);
        }
        return () -> {
            IntStream
                    .range(0, count)
                    .filter(i -> (i % BATCH_SIZE) == 0)
                    .forEach(i -> dataGeneratorService.storeInDatabaseRandomRecords(BATCH_SIZE));
            return String.format("Records %d stored in database", count);
        };
    }

}
