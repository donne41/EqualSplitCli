package org;

public class Person {
    private String name;
    private double moneySpent;
    private double moneyOwnd;
    private double debt;

    public Person(String name, double moneySpent) {
        this.name = name;
        this.moneySpent = moneySpent;
    }

    public double getMoneyOwnd() {
        return moneyOwnd;
    }

    public void setMoneyOwnd(double moneyOwnd) {
        this.moneyOwnd = moneyOwnd;
    }

    public String getName() {
        return name;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double money) {
        moneySpent = money;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return "Person" +
                " name=" + name + '\'' +
                ", moneySpent=" + moneySpent;
    }
}
