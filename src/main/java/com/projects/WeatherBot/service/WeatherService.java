package com.projects.WeatherBot.service;

import com.projects.WeatherBot.entity.WeatherData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WeatherService {
    private final WebClient webClient;

    private final String id;

    @Autowired
    public WeatherService(WebClient webClient, @Value("${weather.id}") String id) {
        this.webClient = webClient;
        this.id = id;

    }

    public WeatherData getCurrentWeatherByName(final String name) {
        return webClient
                .get()
                .uri(String.join("", "/data/2.5/weather?q=", name, "&appid=", id, "&units=metric&lang=ru"))
                .retrieve()
                .bodyToMono(WeatherData.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new WeatherData()))
                .block();
    }

}
