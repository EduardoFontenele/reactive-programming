package com.reactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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

    public Flux<String> getFruitsMapDoOnNext() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .doOnNext(s -> System.out.println("s - " + s))
                .doOnSubscribe(subscription -> System.out.println("Subscription: " + subscription))
                .log();
    }

    public Flux<String> getFruitsOnErrorReturn() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .concatWith(Flux.error(new RuntimeException()))
                .onErrorReturn("Mango");
    }

    public Flux<String> getFruitsOnErrorContinue() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .map(str -> {
                    if (str.equalsIgnoreCase("apple")) throw new RuntimeException("Fruit Exception");
                    return str.toUpperCase();
                })
                .onErrorContinue((err, obj) -> {
                    log.error(err.getLocalizedMessage());
                    log.info("Object that failed: {}", obj.toString());
                });
    }

    public Flux<String> getFruitsOnErrorMap() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .map(str -> {
                    if (str.equalsIgnoreCase("apple")) throw new RuntimeException("Fruit Exception");
                    return str.toUpperCase();
                })
                .onErrorMap(throwable -> new IllegalStateException(throwable.getLocalizedMessage() + " FROM ERROR MAP"));
    }

    public Flux<String> getFruitsDoOnError() {
        return Flux.fromIterable(List.of("Banana", "Apple", "Watermelon"))
                .map(str -> {
                    if (str.equalsIgnoreCase("apple")) throw new RuntimeException("Fruit Exception");
                    return str.toUpperCase();
                })
                .doOnError(throwable -> log.error("{} FROM ERROR MAP", throwable.getLocalizedMessage()));
    }

    public Flux<String> getFruitsMap() {
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

    public Flux<String> getFruitsConcatWith() {
        var fruits1 = Flux.just("Banana", "Pineapple");
        var fruits2 = Flux.just("Watermelon", "Strawberry");

        return fruits1.concatWith(fruits2).log();
    }

    public Flux<String> getFruitsConcat() {
        var fruits1 = Flux.just("Banana", "Pineapple");
        var fruits2 = Flux.just("Watermelon", "Strawberry");

        return Flux.concat(fruits2, fruits1).log();
    }

    public Flux<String> getFruitsMerge() {
        var fruits1 = Flux.just("Banana", "Pineapple").delayElements(Duration.ofMillis(100));
        var fruits2 = Flux.just("Watermelon", "Strawberry").delayElements(Duration.ofMillis(125));

        return Flux.merge(fruits2, fruits1).log();
    }

    public Flux<String> getFruitsZip() {
        var fruits1 = Flux.just("Banana", "Pineapple");
        var fruits2 = Flux.just("Watermelon", "Strawberry");

        return Flux.zip(fruits1, fruits2, (fruit1, fruit2) -> fruit1 + fruit2).log();
    }

    public Flux<String> getVegetableFlatMapMany() {
        return Mono.just("Carrot")
                .flatMapMany(str -> Flux.just(str.split("")))
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
}
