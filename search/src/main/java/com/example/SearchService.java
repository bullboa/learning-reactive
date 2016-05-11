package com.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.CompletableFuture;

@Service
public class SearchService {

    RestTemplate restTemplate;

    public SearchService() {
        restTemplate = new RestTemplate();
    }

    public String callApi(String input) {
        return restTemplateCall(input);
    }

    public CompletableFuture<String> callFutureApi(String input) {
        return CompletableFuture.supplyAsync(
                () -> restTemplateCall(input)
        );
    }

    public Observable<String> callObservableApi(String input) {
        return Observable.<String>create(s -> {
            String result = restTemplateCall(input);
            s.onNext(result);
            s.onCompleted();
        });
    }

    private String restTemplateCall(String input) {
        return restTemplate.getForEntity("http://localhost:8081/searchDuckDuckGo?input=" + input, String.class).getBody();
    }
}
