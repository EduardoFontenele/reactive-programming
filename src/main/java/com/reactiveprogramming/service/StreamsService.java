package com.reactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@Slf4j
public class StreamsService {

    private final Function<String, String> toUpper = String::toUpperCase;
    private final Predicate<String> bigStrings = str -> str.length() > 5;

    public Flux<String> getFruits() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon")).log();
    }

    public Flux<String> getFruitsMap() {
        log.info("Calling fruits map");
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .map(toUpper)
                .log();
    }

    public Flux<String> getFruitsFilter() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .filter(bigStrings)
                .log();
    }

    public Flux<String> getFruitsFlatMap() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .flatMap(s -> Flux.just(s.split("")))
                .log();
    }

    public Flux<String> getFruitsTransform(int size) {
        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > size);

        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .transform(filterData)
                .defaultIfEmpty("Default")
                .log();
    }

    public Flux<String> getFruitsTransformSwitchIfEmpty(int size) {
        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > size);

        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .transform(filterData)
                .switchIfEmpty(Flux.just("Pineapple", "Strawberry"))
                .log();
    }

    public Mono<String> getVegetable() {
        return Mono.just("Carrot").log();
    }

    public Mono<List<String>> getVegetableFlatMap() {
        return Mono.just("Carrot")
                .flatMap(str -> Mono.just(List.of(str.split(""))))
                .log();
    }

    public Flux<String> getVegetableFlatMapMany() {
        return Mono.just("Carrot")
                .flatMapMany(str -> Flux.just(str.split("")))
                .log();
    }


}
