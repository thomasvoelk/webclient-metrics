package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.context.ContextView;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LogResponseTimeFilterFunction implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).as(this::instrumentResponse)
                .contextWrite(context -> context.put("start", System.nanoTime()));
    }

    private Mono<ClientResponse> instrumentResponse(Mono<ClientResponse> responseMono) {
        final AtomicBoolean responseReceived = new AtomicBoolean();
        return Mono.deferContextual((ctx) -> responseMono.doOnEach((signal) -> {
            if (signal.isOnNext() || signal.isOnError()) {
                responseReceived.set(true);
                logResponseTime(ctx);
            }
        }).doFinally((signalType) -> {
            if (!responseReceived.get() && SignalType.CANCEL.equals(signalType)) {
                logResponseTime(ctx);
            }
        }));
    }

    private void logResponseTime(ContextView ctx) {
        long start = ctx.get("start");
        Duration duration = Duration.ofNanos(System.nanoTime() - start);
        System.out.println("Duration:" + duration.toMillis());
    }
}
