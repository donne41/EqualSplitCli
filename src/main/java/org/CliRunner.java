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
        System.out.println("Input name or index of person you want to edit");
        String personInput = sc.nextLine().trim();
        System.out.println("Input a corrected amount of money spent");
        String newMoney = sc.nextLine().trim();
        editMoneySpent(personInput, newMoney);

    }

    protected void editMoneySpent(String name, String money) {
        var person = findPerson(name);
        if (person.isEmpty()) {
            System.out.println("Could not find person!");
            return;
        }
        if (!moneyValidation(money)) {
            System.out.println("invalid money input!");
            return;
        }
        System.out.println(person.get().getName() + " " + person.get().getMoneySpent() + " -> " + money);
        person.get().setMoneySpent(Double.parseDouble(money));
    }

    private Optional<Person> findPerson(String name) {
        if (name.matches("\\d")) {
            if (list.size() < Integer.parseInt(name)) return Optional.empty();
            return Optional.of(list.get(Integer.parseInt(name)));
        }
        return list.stream().filter(p ->
                p.getName().matches(name)).findFirst();
    }

    private boolean moneyValidation(String input) {
        if (input.matches("\\d+")) {
            return true;
        }
        return false;
    }

    private void removePerson() {
        System.out.println("Enter name or index of person to be removed");
        String input = sc.nextLine().trim();
//        if (index >= list.size()) {
//            System.out.println("index out of bounds!");
//            return;
//        }
//        if (index < 0) {
//            var person = findPerson(input);
//            System.out.println("Remove person: " + person.get().getName() + " " + person.get().getMoneySpent());
//            System.out.println("1) yes \nany) Exit");
//            String answer = sc.nextLine().trim();
//            if (answer.matches("1")) {
//                list.remove(person);
//                System.out.println("Removed person");
//            }
//        } else {
//            var person = list.get(index);
//            System.out.println("Remove person: " + person.getName() + " " + person.getMoneySpent());
//            System.out.println("1) yes \nany) Exit");
//            String answer = sc.nextLine().trim();
//            if (answer.matches("1")) {
//                list.remove(person);
//                System.out.println("Removed person");
//            }
//        }

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
