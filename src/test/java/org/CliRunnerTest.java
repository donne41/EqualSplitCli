package org;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        cRunner.run();
        var result = cRunner.service.getGroup().size();

        assertThat(result).isEqualTo(1);
    }


    @Test
    void addThreePersonShouldReturnCorrectSize() {
        String input = "1\nbob\n100\n1\ntim\n200\n1\nrick\n300\n9";
        Scanner fakeScanner = new Scanner(input);
        cRunner = new CliRunner(fakeScanner);

        cRunner.run();
        var result = cRunner.service.getGroup().size();

        assertThat(result).isEqualTo(3);
    }

    @ParameterizedTest
    @CsvSource({
            "3, bob, 1, 3, tim, anyKey, 9"
    })
    void removePersonShouldWithConfirmationShouldDecreaseSizeButNotAnyKey(int menuSelect, String name,
                                                                          int confirm, int menuSelect2,
                                                                          String name2, String anyKey, String exit) {
        String simInput = String.format("%d\n%s\n%d\n%d\n%s\n%s\n%s", menuSelect, name, confirm, menuSelect2, name2, anyKey, exit);
        Scanner fakeScanner = new Scanner(simInput);
        cRunner = new CliRunner(fakeScanner);
        cRunner.service.addPerson("bob", 200);
        cRunner.service.addPerson("tim", 300);

        cRunner.run();

        var result = cRunner.service.getGroup().size();

        assertThat(result).isEqualTo(1);
    }

    @Test
    void editedPersonShouldSaveNewSpentMoney() {
        cRunner = new CliRunner();
        cRunner.service.addPerson("bob", 100);
        cRunner.service.editPerson("bob", 300);

        var result = cRunner.service.getGroup().getLast().getMoneySpent();
        assertThat(result).isEqualTo(300);

    }

    @Test
    void editPersonWrongInputValidation() {
        String blankInput = "2\n   ";
        String noMoneyInput = "2\niCri\nNoMoney";
        Scanner fakeScan1 = new Scanner(blankInput);
        Scanner fakeScan2 = new Scanner(noMoneyInput);
        cRunner = new CliRunner(fakeScan1);

        assertThrows(IllegalArgumentException.class, () -> {
            cRunner.editPerson();
        });
        cRunner.switchScanner(fakeScan2);

        assertThrows(IllegalArgumentException.class, () -> {
            cRunner.editPerson();
        });
    }

    @Test
    void findPersonShouldReturnPersonWithDifferentMethods() {
        cRunner = new CliRunner();
        cRunner.service.addPerson("bob", 200);

        var stringResult = cRunner.findPerson("bob");
        var indexResult = cRunner.findPerson("0");


        assertThat(stringResult).isNotEmpty();
        assertThat(indexResult).isNotEmpty();
    }

    @Test
    void removePersonShouldReduceListCountByOne() {
        String simInput = "bob\n1";
        Scanner fakeScanner = new Scanner(simInput);
        cRunner = new CliRunner(fakeScanner);
        cRunner.service.addPerson("bob", 100);
        cRunner.service.addPerson("tim", 200);
        cRunner.removePerson();
        var result = cRunner.service.getGroup().size();

        assertThat(result).isOne();
    }

    @Test
    void calculateWithValidGroupShouldReturnHistorySize() {
        cRunner = new CliRunner();
        cRunner.service.addPerson("bob", 200);
        cRunner.service.addPerson("tim", 400);

        cRunner.calculate();

        var result = cRunner.service.getResultList().size();

        assertThat(result).isOne();
    }

}
