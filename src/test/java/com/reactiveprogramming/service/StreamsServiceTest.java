package com.reactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StreamsServiceTest {

    @InjectMocks
    private StreamsService service;

    @Test
    void testGetFruits() {
        var result = service.getFruits();
        StepVerifier.create(result).expectNext("Banana", "Apple", "Watermelon").verifyComplete();
    }

    @Test
    void testGetVegetable() {
        var result = service.getVegetable();
        StepVerifier.create(result).expectNext("Carrot").verifyComplete();
    }

    @Test
    void getFruitsMap() {
        var result = service.getFruitsMap();
        StepVerifier.create(result).expectNext("BANANA", "APPLE", "WATERMELON").verifyComplete();
    }

    @Test
    void getFruitsFilter() {
        var result = service.getFruitsFilter();
        StepVerifier.create(result).expectNext("Banana", "Watermelon").verifyComplete();
    }

    @Test
    void getFruitsFlatMap() {
        var result = service.getFruitsFlatMap();
        StepVerifier.create(result).expectNextCount(21L).verifyComplete();
    }

    @Test
    void getVegetableFlatMap() {
        var result = service.getVegetableFlatMap();
        StepVerifier.create(result).expectNextCount(1L).verifyComplete();
    }

    @Test
    void getVegetableFlatMapMany() {
        var result = service.getVegetableFlatMapMany();
        StepVerifier.create(result).expectNextCount(6L).verifyComplete();
    }

    @Test
    void getFruitsTransform() {
        var result = service.getFruitsTransform(5);
        StepVerifier.create(result).expectNextCount(2L).verifyComplete();
    }

    @Test
    void getFruitsEmpty() {
        var result = service.getFruitsTransform(10);
        StepVerifier.create(result).expectNextCount(1L).verifyComplete();
    }

    @Test
    void getFruitsTransformSwitchIfEmpty() {
        var result = service.getFruitsTransformSwitchIfEmpty(10);
        StepVerifier.create(result).expectNextCount(2L).verifyComplete();
    }

    @Test
    void getFruitsConcatWith() {
        var result = service.getFruitsConcatWith();
        StepVerifier.create(result).expectNextCount(4L).verifyComplete();
    }

    @Test
    void getFruitsConcat() {
        var result = service.getFruitsConcat();
        StepVerifier.create(result).expectNextCount(4L).verifyComplete();
    }

    @Test
    void getFruitsMerge() {
        var result = service.getFruitsMerge();
        StepVerifier.create(result).expectNextCount(4L).verifyComplete();
    }

    @Test
    void getFruitsZip() {
        var result = service.getFruitsZip();
        StepVerifier.create(result).expectNextCount(2L).verifyComplete();
    }

    @Test
    void getFruitsMapDoOnNext() {
        var result = service.getFruitsMapDoOnNext();
        StepVerifier.create(result).expectNextCount(3L).verifyComplete();
    }

    @Test
    void getFruitsOnErrorReturn() {
        var result = service.getFruitsOnErrorReturn().log();
        StepVerifier.create(result).expectNext("Banana", "Apple", "Watermelon", "Mango").verifyComplete();
    }

    @Test
    void getFruitsOnErrorContinue() {
        var result = service.getFruitsOnErrorContinue().log();
        StepVerifier.create(result).expectNext("BANANA", "WATERMELON").verifyComplete();
    }

    @Test
    void getFruitsOnErrorMap() {
        var result = service.getFruitsOnErrorMap().log();
        StepVerifier.create(result).expectNext("BANANA").expectError(IllegalStateException.class).verify();
    }

    @Test
    void getFruitsDoOnError() {
        var result = service.getFruitsOnErrorMap().log();
        StepVerifier.create(result).expectNext("BANANA").expectError(RuntimeException.class).verify();
    }
}