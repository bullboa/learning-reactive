package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import java.util.concurrent.CompletableFuture;

@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @ResponseBody
    @RequestMapping("/1/{input}")
    public String index(@PathVariable(value = "input") String input) {
        return searchService.callApi(input);
    }

    @ResponseBody
    @RequestMapping("/2/{input}")
    public DeferredResult<String> index2(@PathVariable(value = "input") String input) {
        DeferredResult<String> deferredResult = new DeferredResult<>(10000L);
        CompletableFuture<String> completableFuture = searchService.callFutureApi(input);
        completableFuture.whenComplete((res, ex) -> {
            if (ex != null) {
                deferredResult.setErrorResult(ex);
            } else {
                deferredResult.setResult(res);
            }
        });
        return deferredResult;
    }

    @ResponseBody
    @RequestMapping("/3/{input}")
    public DeferredResult<String> index3(@PathVariable(value = "input") String input) {
        DeferredResult<String> deferredResult = new DeferredResult<>(10000L);
        Observable<String> observable = searchService.callObservableApi(input);
        observable.subscribe(deferredResult::setResult, deferredResult::setErrorResult);
        return deferredResult;
    }
}
