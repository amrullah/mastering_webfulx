package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisherTest() throws InterruptedException {
        // by default it's a cold publisher. emits all the values from the beginning, for each subscriber
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(s -> System.out.println("Subscribe 1 : " + s));

        Thread.sleep(2000);

        stringFlux.subscribe(s -> System.out.println("Subscribe 2 : " + s));

        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();

        connectableFlux.connect();

        connectableFlux.subscribe(s -> System.out.println("Subscribe 1 : " + s));
        Thread.sleep(2000);

        // does not receive values from the beginning. Receives from when it subscribes
        connectableFlux.subscribe(s -> System.out.println("Subscribe 2 : " + s));
        Thread.sleep(4000);
    }
}
