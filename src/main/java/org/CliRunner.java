package org;

import java.util.Optional;
import java.util.Scanner;

public class CliRunner {
    private Scanner sc;
    boolean badInput;
    EqualSplitService service = new EqualSplitService();

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

    public void welcome() {
        System.out.printf("""
                Welcome to EqualSplit!
                
                I will help you calculate everyone's share of the costs.
                If someone has paid more than their share,
                I'll figure out exactly who owes whom—minimizing the number of transactions to make settling up as easy as possible.
                
                """);
        run();
    }


    public void run() {
        badInput = true;
        do {
            try {
                System.out.printf("""
                        1) Add person to group
                        2) Edit person
                        3) Remove person
                        4) View group
                        5) Calculate
                        any) Exit
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
                System.out.println("Error: " + e.getMessage() + "\n");
                run();
            }


        } while (badInput);
    }


    private void viewList() {
        var group = service.getGroup();
        group.forEach(p -> System.out.println(p));
    }


    void addPerson() {
        System.out.println("Name: ");
        String inputName = sc.nextLine().trim();
        if (inputName.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        String name = getNormilizedName(inputName);
        System.out.println("Money spent: ");
        double moneySpent = getSafeDouble(sc.nextLine().trim());
        if (moneyValidation(moneySpent)) throw new IllegalArgumentException("Bad money input");

        service.addPerson(name, moneySpent);
    }


    private static String getNormilizedName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    void editPerson() throws IllegalArgumentException {
        System.out.println("Input name or index of person you want to edit");
        String personInput = sc.nextLine().trim();
        if (personInput.isBlank()) throw new IllegalArgumentException("No name found or incorrect input");
        String name = getNormilizedName(personInput);

        System.out.println("Input a corrected amount of money spent");
        double newMoney = getSafeDouble(sc.nextLine().trim());
        if (moneyValidation(newMoney)) throw new IllegalArgumentException("Incorrect money input");

        double oldSpent = service.getPersonSpent(name);

        if (service.editPerson(name, newMoney)) {
            System.out.println("name " + oldSpent + " -> " + newMoney);
        } else {
            System.out.println(name + " not found!");
        }

    }

    Optional<Person> findPerson(String name) {
        if (name.matches("\\d+")) {
            if (service.getGroup().size() <= Integer.parseInt(name)) return Optional.empty();
            return Optional.of(service.findPerson(Integer.parseInt(name)));
        }
        return service.findPerson(getNormilizedName(name));
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
        String name = getNormilizedName(input);
        var person = service.findPerson(name);
        if (person.isEmpty()) throw new IllegalArgumentException("No person found!");
        System.out.println("Remove person: " + person.get().getName() + " " + person.get().getMoneySpent());
        System.out.println("1) yes \nany) Exit");
        if (sc.nextLine().matches("1")) {
            if (service.removePerson(name))
                System.out.println("Person removed!");
        }
    }

    void calculate() {
        service.calculate();
        resultPrinter();
    }

    private void resultPrinter() {
        var result = service.getResultList().getLast();
        if (result.transactions.isEmpty()) {
            System.out.println("No transactions needs to be made!");
        }
        for (Transaction t : result.transactions) {
            System.out.printf("""
                    Sender: %-10s -> Reciver: %-10s Amount: %.2f\n""", t.getSenderName(), t.getReciverName(), t.getMoney());
        }
        System.out.printf("""
                Total Expenses: %.2f | Individual Share: %.2f\n""", result.sum, result.portion);
    }

}
