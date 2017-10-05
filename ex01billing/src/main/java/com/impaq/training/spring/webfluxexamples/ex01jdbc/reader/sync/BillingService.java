package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync;

import static com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.JdbcReaderConfiguration.SQL_QUERY;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impaq.training.spring.webfluxexamples.ex01jdbc.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<BillingRecord> findBillingRecords() {
        log.info("Loading all billing data");
        return jdbcTemplate.query(SQL_QUERY, new BeanPropertyRowMapper<>(BillingRecord.class));
    }
}
