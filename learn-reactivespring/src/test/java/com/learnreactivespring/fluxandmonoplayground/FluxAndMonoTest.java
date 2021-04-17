package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After Error"))
                .log();

        stringFlux.subscribe(System.out::println,
                (e) -> System.err.println("Exception is: " + e),
                () -> System.out.println("Completed"));  // won't be printed if there's an error in the flux
    }
    @Test
    public void fluxTestElements_WithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")  // changing the order will fail the test
                .expectNext("Reactive Spring")
                .verifyComplete();  // if this is removed, then the flow of data from flux simply won't start.
    }

    @Test
    public void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")  // also possible to supply multiple values here
                .expectError(RuntimeException.class)
                .verify();  // if this is removed, then the flow of data from flux won't start.
        // verifyComplete doesn't make sense here, because complete event is not triggered if error occurs
    }

    @Test
    public void fluxTestElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occurred")// either this or expectError() will be allowed, not both
                .verify();  // if this is removed, then the flow of data from flux won't start.
        // verifyComplete doesn't make sense here, because complete event is not triggered if error occurs
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring").log();

        StepVerifier.create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error() {

        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
