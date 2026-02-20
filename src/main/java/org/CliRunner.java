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


    protected void addPerson() {
        String name;
        String moneySpent;
        do {
            System.out.println("Name: ");
            name = sc.nextLine().trim();
            System.out.println("Money spent: ");
            moneySpent = sc.nextLine().trim();
            if (name.isBlank()) System.out.println("Bad name input!");
            if (moneyValidation(moneySpent)) System.out.println("Bad money input");
            break;
        } while (true);
        addPersonToList(name, moneySpent);
    }

    protected void addPersonToList(String name, String moneySpent) {
        String normilizedName = getNormilizedName(name);
        double money = Double.parseDouble(moneySpent);
        list.add(new Person(normilizedName, money));
    }

    private static String getNormilizedName(String name) {
        String normilizedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return normilizedName;
    }

    private void editPerson() {
        String personInput;
        String newMoney;
        do {
            System.out.println("Input name or index of person you want to edit");
            personInput = sc.nextLine().trim();
            if (personInput.isBlank()) System.out.println("No name found or incorrect input");
            System.out.println("Input a corrected amount of money spent");
            newMoney = sc.nextLine().trim();
            if (moneyValidation(newMoney)) System.out.println("Incorrect money input");
            break;
        } while (true);
        editMoneySpent(personInput, newMoney);

    }

    protected void editMoneySpent(String name, String money) {
        var person = findPerson(getNormilizedName(name));
        System.out.println(person.get().getName() + " " + person.get().getMoneySpent() + " -> " + money);
        person.get().setMoneySpent(Double.parseDouble(money));
    }

    protected Optional<Person> findPerson(String name) {
        if (name.matches("\\d+")) {
            if (list.size() <= Integer.parseInt(name)) return Optional.empty();
            return Optional.of(list.get(Integer.parseInt(name)));
        }
        return list.stream().filter(p ->
                p.getName().matches(getNormilizedName(name))).findFirst();
    }

    protected boolean moneyValidation(String input) {
        if (input.matches("\\d+")) {
            return false;
        }
        return true;
    }

    protected void removePerson() {
        System.out.println("Enter name or index of person to be removed");
        String input = sc.nextLine().trim();
        var person = findPerson(input);
        if (person.isEmpty()) {
            System.out.println("Person not found");
            return;
        }
        System.out.println("Remove person: " + person.get().getName() + " " + person.get().getMoneySpent());
        System.out.println("1) yes \nany) Exit");
        if (sc.nextLine().matches("1")) {
            list.remove(person.get());
            System.out.println("Person removed!");
        }
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
        System.out.println("Total spent: " + result.sum + " Share amount: " + result.portion);
    }

}
