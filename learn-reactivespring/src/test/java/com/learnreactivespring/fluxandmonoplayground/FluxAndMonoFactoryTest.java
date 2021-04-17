package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {
    List<String> names = Arrays.asList("Veeru", "Gabbar", "Samba", "Jai");
    @Test
    public void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Veeru", "Gabbar", "Samba", "Jai")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[]{"Veeru", "Gabbar", "Samba", "Jai"};
        Flux<String> namesFlux = Flux.fromArray(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Veeru", "Gabbar", "Samba", "Jai")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(names.stream());
        StepVerifier.create(namesFlux)
                .expectNext("Veeru", "Gabbar", "Samba", "Jai")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log()).verifyComplete();  // expectNext won't work here
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "adam";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        System.out.println(stringSupplier.get());  // mono takes care of Supplier.get()

        StepVerifier.create(stringMono.log())
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux.log())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
