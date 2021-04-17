package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {
        /*
        .filter() returns a new flux whose elements satisfy the filter function specified
         */
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("a"))  // adam, anna
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna")
                .verifyComplete();
    }

    @Test
    public void filterTestLength() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)  // jenny
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("jenny")
                .verifyComplete();
    }
}
