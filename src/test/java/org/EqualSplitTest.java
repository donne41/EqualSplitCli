package org;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EqualSplitTest {


    EqualSplit EQ = new EqualSplit();

    List<Person> getExampleList() {
        Person p1 = new Person("One", 100);
        Person p2 = new Person("Two", 200);
        Person p3 = new Person("Three", 300);
        Person p4 = new Person("Four", 400);
        Person p5 = new Person("Five", 500);
        return List.of(p1, p2, p3, p4, p5);
    }

    @Test
    void getSum() {
        var result = EQ.getSum(getExampleList());

        assertThat(result).isEqualTo(1500);
    }

    @Test
    void getPortionSize() {
        List<Person> list = getExampleList();
        var sum = EQ.getSum(list);
        var result = EQ.getPortionSize(list, sum);

        assertThat(result).isEqualTo(300);
    }
}
