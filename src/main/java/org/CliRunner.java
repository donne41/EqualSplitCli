package org;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public void switchScanner(Scanner newScanner) {
        sc.close();
        sc = newScanner;
    }

    public CliRunner() {
        sc = new Scanner(System.in);
    }


    public void run() {
        badInput = true;
        System.out.printf("""
                Welcome to EqualSplit!
                
                I will help you calculate everyone's share of the costs.
                If someone has paid more than their share,
                I'll figure out exactly who owes whom—minimizing the number of transactions to make settling up as easy as possible.
                
                """);
        do {
            try {
                System.out.printf("""
                        1) Add person to group
                        2) edit person
                        3) remove person
                        4) view group
                        5) calculate
                        9) exit
                        """);
                String input = sc.nextLine().trim();
                MenuSelect select = MenuSelect.fromString(input);
                switch (select) {
                    case ADD -> addPerson();
                    case EDIT -> editPerson();
                    case REMOVE -> removePerson();
                    case VIEW -> viewList();
                    case CALC -> calculate();
                    case EXIT -> badInput = false;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                run();
            }


        } while (badInput);
    }


    private void viewList() {
        list.forEach(System.out::println);
    }


    void addPerson() {
        System.out.println("Name: ");
        String name = sc.nextLine().trim();
        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");

        System.out.println("Money spent: ");
        double moneySpent = getSafeDouble(sc.nextLine().trim());
        if (moneyValidation(moneySpent)) throw new IllegalArgumentException("Bad money input");

        addPersonToList(name, moneySpent);
    }

    void addPersonToList(String name, double money) {
        String normilizedName = getNormilizedName(name);
        list.add(new Person(normilizedName, money));
    }

    private static String getNormilizedName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    void editPerson() throws IllegalArgumentException {
        System.out.println("Input name or index of person you want to edit");
        String personInput = sc.nextLine().trim();
        if (personInput.isBlank()) throw new IllegalArgumentException("No name found or incorrect input");

        System.out.println("Input a corrected amount of money spent");
        double newMoney = getSafeDouble(sc.nextLine().trim());
        if (moneyValidation(newMoney)) throw new IllegalArgumentException("Incorrect money input");

        editMoneySpent(personInput, newMoney);

    }

    void editMoneySpent(String name, double money) {
        var person = findPerson(getNormilizedName(name));
        if (person.isEmpty()) throw new IllegalArgumentException("No person found!");
        System.out.println(person.get().getName() + " " + person.get().getMoneySpent() + " -> " + money);
        person.get().setMoneySpent(money);
    }

    Optional<Person> findPerson(String name) {
        if (name.matches("\\d+")) {
            if (list.size() <= Integer.parseInt(name)) return Optional.empty();
            return Optional.of(list.get(Integer.parseInt(name)));
        }
        return list.stream().filter(p ->
                p.getName().matches(getNormilizedName(name))).findFirst();
    }

    private boolean moneyValidation(double input) {
        if (input >= 0) {
            return false;
        }
        return true;
    }

    private double getSafeDouble(String money) {
        String cleanInput = money.replace(',', '.');
        try {
            return Double.parseDouble(cleanInput);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    void removePerson() throws IllegalArgumentException {
        System.out.println("Enter name or index of person to be removed");
        String input = sc.nextLine().trim();
        var person = findPerson(input);
        if (person.isEmpty()) throw new IllegalArgumentException("No person found!");
        System.out.println("Remove person: " + person.get().getName() + " " + person.get().getMoneySpent());
        System.out.println("1) yes \nany) Exit");
        if (sc.nextLine().matches("1")) {
            list.remove(person.get());
            System.out.println("Person removed!");
        }
    }

    void calculate() {
        history.add(equalSplit.getResult(list));
        resultPrinter();
    }

    private void resultPrinter() {
        var result = history.getLast();
        if (result.transactions.size() == 0) {
            System.out.println("No transactions needs to be made!");
        }
        for (Transaction t : result.transactions) {
            System.out.printf("""
                    Sender: %-10s -> Reciver: %-10s Amount: %.2f\n""", t.getSenderName(), t.getReciverName(), t.getMoney());
        }
        System.out.printf("""
                Total spent: %.2f Share amount: %.2f\n""", result.sum, result.portion);
    }

}
