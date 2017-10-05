package com.impaq.training.spring.webfluxexamples.ex01jdbc.reader.async;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.http.HttpStatus;

import com.impaq.training.spring.webfluxexamples.ex01jdbc.BillingRecord;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AsyncBillingServlet extends HttpServlet {

    private final AsyncBillingService asyncBillingService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Asynchronous processing of getting billing record started");
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0);
        asyncContext.start(() -> processAsynchronously(asyncContext));
        log.info("Servlet method invocation completed");
    }

    @SneakyThrows
    private void processAsynchronously(AsyncContext asyncContext) {
        HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
        ServletOutputStream outputStream = resp.getOutputStream();
        Consumer<Throwable> errorHandler = errorHandler(asyncContext);
        asyncBillingService.findBillingRecords()
                .subscribeOn(Schedulers.io())
                .map(BillingRecord::toCsvString)
                .subscribe(outputStream::println, errorHandler, asyncContext::complete);
    }

    private Consumer<Throwable> errorHandler(AsyncContext asyncContext){
        return ex -> onError(ex, asyncContext, (HttpServletResponse) asyncContext.getResponse());
    }

    @SneakyThrows
    private void onError(Throwable throwable, AsyncContext asyncContext, HttpServletResponse resp) {
        log.error("Error during getting billing data", throwable);
        resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Oops, something went wrong: " + throwable.getMessage());
        asyncContext.complete();
    }
}
