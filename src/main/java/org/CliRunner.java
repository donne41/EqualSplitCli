package org;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliRunner {
    private Scanner sc;
    List<Person> list = new ArrayList<>();
    EqualSplit equalSplit = new EqualSplit();
    List<SplitResult> history = new ArrayList<>();
    boolean badInput;

    public CliRunner(Scanner scanner) {
        sc = scanner;
    }

    public CliRunner() {
        sc = new Scanner(System.in);
    }

    protected void exitProgram() {
        badInput = false;
    }

    void main() {
        badInput = true;
        System.out.printf("""
                Welcome to EqualSplit, Here you can easily calculate a share everybody is support to share
                and if someone has paid more than that they should receive money from people who has
                paid less than the share!
                """);
        do {
            System.out.printf("""
                    1) Add person to group
                    2) edit person
                    3) remove person
                    4) view group
                    5) calculate
                    9) exit
                    """);
            String input = sc.nextLine().trim();
            int i = checkInput(input);
            switch (i) {
                case 1 -> addPerson();
                case 2 -> editPerson();
                case 3 -> removePerson();
                case 4 -> viewList();
                case 5 -> calculate();
                default -> badInput = false;
            }

        } while (badInput);

    }

    private int checkInput(String input) {
        int i = 0;
        try {
            i = Integer.parseInt(input);
        } catch (NumberFormatException _) {
            i = 20;
        }
        return i;
    }

    private void viewList() {
        list.forEach(System.out::println);
    }

    private void addPerson() {
        System.out.println("Name: ");
        String name = sc.nextLine().trim();
        while (true) {
            System.out.println("Money spent: ");
            double money = 0;
            try {
                money = Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Error money input for person");
                continue;
            }
            if (money < 0) {
                System.out.println("A person should not have gained money ie. Negative money spent. Try again.");
                continue;
            }
            list.add(new Person(name, money));
            System.out.println("Added person: " + list.getLast().getName());
            return;
        }

    }

    private void editPerson() {
        list.addAll(getExampleList());
        calculate();

    }

    private void removePerson() {

    }

    private void calculate() {
        history.add(equalSplit.getResult(list));
        resultPrinter();
    }

    private void resultPrinter() {
        var result = history.getLast();
        for (Transaction t : result.transactions) {
            System.out.printf("""
                    Sender: %-10s -> Reciver: %-10s Amount: %.2f\n""", t.getSenderName(), t.getReciverName(), t.getMoney());
        }
    }

    List<Person> getExampleList() {
        Person p1 = new Person("One", 100);
        Person p2 = new Person("Two", 200);
        Person p3 = new Person("Three", 300);
        Person p4 = new Person("Four", 400);
        Person p5 = new Person("Five", 500);
        Person p6 = new Person("Six", 600);
        return List.of(p1, p2, p3, p4, p5, p6);
    }
}
