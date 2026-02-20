package org;

import org.junit.jupiter.api.Test;
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

    @ParameterizedTest
    @CsvSource({
            "3, bob, 1, 3, tim, 1, 9"
    })
    void removePersonShouldWithConfirmationShouldDecreseSizeButNotAnyKey(int menuSelect, String name,
                                                                         int confirm, int menuSelect2,
                                                                         String name2, String anyKey, String exit) {
        String simInput = String.format("%d\n%s\n%d\n%d\n%s\n%s\n%s", menuSelect, name, confirm, menuSelect2, name2, anyKey, exit);
        Scanner fakeScanner = new Scanner(simInput);
        cRunner = new CliRunner(fakeScanner);
        cRunner.list.add(new Person("bob", 200));
        cRunner.list.add(new Person("tim", 300));

        cRunner.main();

        var result = cRunner.list.size();

        assertThat(result).isEqualTo(1);
    }

    @Test
    void editedPersonShouldSaveNewSpentMoney() {
        cRunner = new CliRunner();
        cRunner.list.add(new Person("bob", 100));
        cRunner.editMoneySpent("bob", "300");

        var result = cRunner.list.getLast().getMoneySpent();
        assertThat(result).isEqualTo(300);

    }

    @Test
    void findPersonShouldReturnPersonWithDifferentMethods() {
        cRunner = new CliRunner();
        cRunner.list.add(new Person("bob", 100));

        var stringResult = cRunner.findPerson("bob");
        var indexResult = cRunner.findPerson("0");

        assertThat(stringResult).isNotEmpty();
        assertThat(indexResult).isNotEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "Bob, 1"
    })
    void removePersonShouldReduceListCountByOne(String name, String confirm) {
        String simInput = String.format("%s\n%s", name, confirm);
        Scanner fakeScanner = new Scanner(simInput);
        cRunner = new CliRunner(fakeScanner);
        cRunner.list.add(new Person("Bob", 100));
        cRunner.list.add(new Person("Tim", 200));
        cRunner.removePerson();
        var result = cRunner.list.size();

        assertThat(result).isOne();
    }

}
