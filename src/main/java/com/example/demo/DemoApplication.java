package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final WebClient client;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public DemoApplication(WebClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        client.get().uri("https://www.google.de/").retrieve().bodyToMono(String.class).block(Duration.ofMillis(1000L));
    }
}
