package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        /*
        map() applies the function specified in it, to every element of a flux, and returns a new flux
         */
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase).log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(s -> String.valueOf(s.length())).log();

        StepVerifier.create(namesFlux)
                .expectNext("4", "4", "4", "5")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Repeat() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(s -> String.valueOf(s.length()))
                .repeat(1)  // repeat the flux one more time
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("4", "4", "4", "5")
                .expectNext("4", "4", "4", "5")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Filter() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .repeat(1)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("JENNY", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        /*
        flatMap() is used when db or external service call  returns a flux
        flatMap() is for asynchronous (non-blocking) 1-to-N transformations, while map() is for synchronous 1-to-1 transformations
        see https://stackoverflow.com/a/49169314
         */
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));  // A -> List[A, newValue], B -> List[B, newValue]
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMap_Parallel() {
        /*
        flatMap() is used when db or external service call  returns a flux
        flatMap() is for asynchronous (non-blocking) 1-to-N transformations, while map() is for synchronous 1-to-1 transformations

        window(n) will convert a flux<T> into Flux<Flux<T>> where length of inner fluxes will be n
         */
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap((flux) ->
                    flux.map(this::convertToList).subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_Parallel_OrderPreserve() {
        /*
            concatMap() preserves the order
         */
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMapSequential((flux) ->  // concatMap will also preserve order, but won't give us parallelism
                        flux.map(this::convertToList).subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
