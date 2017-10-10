package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.sync;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BillingServlet extends HttpServlet {

    private final BillingService billingService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader(CONTENT_TYPE, TEXT_PLAIN_VALUE);
        ServletOutputStream outputStream = resp.getOutputStream();
        log.info("Loading billing records");
        outputStream.println("Yo, man!");
        List<BillingRecord> listOfRecords = billingService.findBillingRecords();
        log.info("Sending data to http client");
        listOfRecords.forEach(record -> writeRecord(outputStream, record));
    }

    @SneakyThrows
    private void writeRecord(ServletOutputStream outputStream, BillingRecord billingRecord) {
        outputStream.println(billingRecord.toCsvString());
    }
}
