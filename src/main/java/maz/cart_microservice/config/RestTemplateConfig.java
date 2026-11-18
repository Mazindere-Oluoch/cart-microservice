package maz.cart_microservice.config;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    //NB: User MS and Item MS don't need this unless they also need to call another service.

    @Bean //Makes the RestTemplate available to Spring's application context.
    @LoadBalanced //tells Spring 2 use a load-balancing interceptor when making requests
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
