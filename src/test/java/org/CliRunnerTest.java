package org;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Scanner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CliRunnerTest {
    CliRunner cRunner;


    @ParameterizedTest
    @CsvSource({
            "1, Bob, 100, 9"
    })
    void addPersonShouldIncreaseGroupSize(int menuSelect, String name, double amount, int exit) {
        String simInput = String.format("%d\n%s\n%.0f\n%d\n", menuSelect, name, amount, exit);

        Scanner fakeScanner = new Scanner(simInput);
        cRunner = new CliRunner(fakeScanner);

        cRunner.main();
        var result = cRunner.list.size();

        assertThat(result).isEqualTo(1);

    }

    private static Stream<Arguments> personprovider() {
        return Stream.of(
                Arguments.of(
                        String.join("\n", "1", "bob", "100", "1", "bob2", "200", "1", "bob3", "300", "\n"))
        );
    }

    @ParameterizedTest
    @MethodSource("personprovider")
    void addThreePersonShouldReturnCorrectSize(String input) {
        Scanner fakeScanner = new Scanner(input);
        cRunner = new CliRunner(fakeScanner);

        cRunner.main();
        cRunner.exitProgram();
        var result = cRunner.list.size();

        assertThat(result).isEqualTo(3);
    }

}
