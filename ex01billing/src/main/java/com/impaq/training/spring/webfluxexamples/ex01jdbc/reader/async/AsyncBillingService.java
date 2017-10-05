package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async;

import static com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.JdbcReaderConfiguration.SQL_QUERY;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

import java.sql.PreparedStatement;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.ex01jdbc.BillingRecord;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncBillingService {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<BillingRecord> beanPropertyRowMapper;

    public AsyncBillingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.beanPropertyRowMapper = new BeanPropertyRowMapper(BillingRecord.class);
    }

    public Observable<BillingRecord> findBillingRecords(){
        log.info("Request for creating billing record observable");
        return Observable.create(this::findBillingRecords);
    }

    private void findBillingRecords(ObservableEmitter<BillingRecord> emitter) {
        log.info("Billing record observable created");
        try {
            jdbcTemplate.query(preparedStatementCreator(), createRowCallbackHandler(emitter));
            emitter.onComplete();
            log.info("Getting data from database completed");
        }catch (Throwable ex){
            log.error("Cannot read billing records from database", ex);
            emitter.onError(ex);
        }
    }

    private RowCallbackHandler createRowCallbackHandler(ObservableEmitter<BillingRecord> emitter) {
        return rs -> {
            BillingRecord billingRecord = beanPropertyRowMapper.mapRow(rs, 0);
            emitter.onNext(billingRecord);
        };
    }

    private PreparedStatementCreator preparedStatementCreator(){
        return connection -> {
            //in another thread. @Transactional is pointless so let set transaction to readonly
            connection.setAutoCommit(false);
            connection.setReadOnly(true);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(100);
            log.info("Prepared statement created: {}", preparedStatement);
            return preparedStatement;
        };
    }

}
