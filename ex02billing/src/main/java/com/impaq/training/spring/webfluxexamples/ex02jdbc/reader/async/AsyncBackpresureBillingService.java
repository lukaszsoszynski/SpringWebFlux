package com.impaq.training.spring.webfluxexamples.ex02jdbc.reader.async;

import static com.impaq.training.spring.webfluxexamples.ex02jdbc.reader.JdbcReaderConfiguration.SQL_QUERY;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

import java.sql.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncBackpresureBillingService {

    private final DataSource dataSource;

    public AsyncBackpresureBillingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Flowable<BillingRecord> findBillingRecords(){
        log.info("Request for creating billing record observable");
        return Flowable.generate(this::executeQueryAndGetJdbcObjects, this::extractDatabaseRow, this::closeJdbcObjects);
    }

    private JdbcObjects closeJdbcObjects(JdbcObjects jdbcObjects) {
        log.info("Close jdbc object");
        return jdbcObjects.close();
    }

    private JdbcObjects extractDatabaseRow(JdbcObjects jdbcObjects, Emitter<BillingRecord> emitter) {
        log.debug("Extract database row");
        Optional<BillingRecord> row = jdbcObjects.extractRow();
        row.ifPresent(billingRecord -> emitter.onNext(billingRecord));
        if(!row.isPresent()){
            log.info("No more rows, emit completion event");
            emitter.onComplete();
        }
        return jdbcObjects;
    }

    @SneakyThrows
    private JdbcObjects executeQueryAndGetJdbcObjects() {
        log.info("Execute query and get jdbc object");
        JdbcObjects jdbcObjects = new JdbcObjects(new BeanPropertyRowMapper(BillingRecord.class), dataSource.getConnection());
        return jdbcObjects;
    }

    private final static class JdbcObjects{
        private final BeanPropertyRowMapper<BillingRecord> beanPropertyRowMapper;
        private final Connection connection;
        private final PreparedStatement statement;
        private final ResultSet resultSet;
        private final AtomicInteger atomicInteger;

        @SneakyThrows
        JdbcObjects(BeanPropertyRowMapper<BillingRecord> beanPropertyRowMapper, Connection connection){
            this.beanPropertyRowMapper = beanPropertyRowMapper;
            this.connection = connection;
            this.atomicInteger = new AtomicInteger(0);
            connection.setAutoCommit(false);
            connection.setReadOnly(true);
            statement = connection.prepareStatement(SQL_QUERY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            statement.setFetchSize(100);
            log.info("Prepared statement created: {}", statement);
            this.resultSet = statement.executeQuery();
            log.info("ResultSet created: {}", resultSet);
        }

        @SneakyThrows
        JdbcObjects close(){
            resultSet.close();
            statement.close();
            connection.close();
            log.info("Jdbc objects closed");
            return this;
        }

        @SneakyThrows
        Optional<BillingRecord> extractRow() {
            int rowNumber = atomicInteger.getAndIncrement();
            boolean hasNext = resultSet.next();
            log.debug("Is row {} available {}", rowNumber, hasNext);
            if(hasNext){
                return Optional.of(beanPropertyRowMapper.mapRow(resultSet, rowNumber));
            }
            return Optional.empty();
        }
    }

}
