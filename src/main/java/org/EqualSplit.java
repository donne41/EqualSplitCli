package org;

import java.util.List;

public class EqualSplit {

    public double getSum(List<Person> group) {
        double totalSum = 0;
        for (Person p : group) {
            totalSum += p.getMoneySpent();
        }
        return totalSum;
    }

    public double getPortionSize(List<Person> group, double sum) {
        return sum / group.size();
    }

}
