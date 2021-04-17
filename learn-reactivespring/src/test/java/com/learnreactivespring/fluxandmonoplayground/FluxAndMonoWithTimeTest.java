package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {
    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))  // keeps emitting events infinitely
                .log();

        infiniteFlux.subscribe((element) -> System.out.println("Value is: " + element));

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100))  // keeps emitting events infinitely
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceMap_withDelay() throws InterruptedException {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))  // keeps emitting events infinitely
                .delayElements(Duration.ofSeconds(1))
                .map(l -> l.intValue())
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();

        Thread.sleep(3000);
    }
}
