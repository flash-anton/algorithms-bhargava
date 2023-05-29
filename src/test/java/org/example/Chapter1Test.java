package org.example;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.Chapter1.BinarySearchResult;
import static org.example.Chapter1.binarySearch;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class Chapter1Test {

    private static Stream<Arguments> testParameters() {
        return Stream.of( // listSize, expectedSteps
                Arguments.of(100, 7),
                Arguments.of(240_000, 18),
                Arguments.of(128, 8),
                Arguments.of(256, 9),
                Arguments.of(1_000_000, 20),
                Arguments.of(1_000_000_000, 30)
        );
    }

    @ParameterizedTest
    @MethodSource("testParameters")
    void binarySearch_checkExpectedSteps(int listSize, int expectedSteps) {
        // given
        byte[] list = new byte[listSize];

        // when
        BinarySearchResult bsr = binarySearch(list, (byte) 1);
        log.info("list[{}] required {} steps {}", listSize, bsr.remains().size(), bsr.remains().toString());

        // then
        assertAll(
                () -> assertNull(bsr.index()),
                () -> assertEquals(expectedSteps, bsr.remains().size()));
    }
}
