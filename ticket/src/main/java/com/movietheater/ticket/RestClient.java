package com.movietheater.ticket;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Component
public class RestClient {

    private final RestTemplateBuilder builder;

    public RestClient(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    public <T> T template(Function<RestTemplate, T> function) {
        return function.apply(this.builder.build());
    }

}
