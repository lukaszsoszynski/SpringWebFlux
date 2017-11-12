package com.impaq.training.spring.webfluxexamples.ex01jdbc.datagenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.impaq.training.spring.webfluxexamples.common.RandomRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DataGeneratorService {

    public static final String SQL_INSERT = "insert into billing_record(first_name, last_name, type, start_time, duration) values(?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void storeInDatabaseRandomRecords(int numberOfRecords) {
        log.debug("{} stored in database", numberOfRecords);
        storeInDatabase(generateRandomRecords(numberOfRecords));

    }

    private List<Object[]> generateRandomRecords(int numberOfRecords) {
        return IntStream
                .range(0, numberOfRecords)
                .mapToObj(number -> new RandomRecord())
                .map(RandomRecord::toArrayOfFields)
                .collect(Collectors.toList());
    }

    private void storeInDatabase(List<Object[]> batchArgs) {
        jdbcTemplate.batchUpdate(SQL_INSERT, batchArgs);
    }
}

