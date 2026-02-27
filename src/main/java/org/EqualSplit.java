//package org;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class EqualSplit {
//    ArrayList<Person> debitGroup;
//    ArrayList<Person> creditGroup;
//
//
//    public double getSum(List<Person> group) {
//        double totalSum = 0;
//        for (Person p : group) {
//            totalSum += p.getMoneySpent();
//        }
//        return totalSum;
//    }
//
//    public double getPortionSize(List<Person> group, double sum) {
//        return sum / group.size();
//    }
//
//    public void setMoneyOwned(List<Person> group, double portion) {
//        for (Person p : group) {
//            double balance = portion - p.getMoneySpent();
//            p.setMoneyOwnd(balance);
//            p.setDebt(balance);
//        }
//    }
//
//    public void setPersonCategoryList(List<Person> group) {
//        debitGroup = new ArrayList<>();
//        creditGroup = new ArrayList<>();
//        for (Person p : group) {
//            if (p.getMoneyOwnd() < 0) {
//                debitGroup.add(p);
//            } else {
//                creditGroup.add(p);
//            }
//        }
//        debitGroup.sort(Comparator.comparingDouble(Person::getDebt));
//        creditGroup.sort(Comparator.comparingDouble(Person::getDebt).reversed());
//    }
//
//    public List<Transaction> getDebtCollect() {
//        List<Transaction> debtCollection = new ArrayList<>();
//        int i = 0;
//        int j = 0;
//
//        while (i < debitGroup.size() && j < creditGroup.size()) {
//            Person debitP = debitGroup.get(i);
//            Person creditP = creditGroup.get(j);
//
//            double balance = Math.min((debitP.getMoneyOwnd() * -1), Math.abs(creditP.getMoneyOwnd()));
//
//            if (balance > 0.05) {
//                debtCollection.add(new Transaction(creditP.getName(), balance, debitP.getName()));
//            }
//            debitP.setMoneyOwnd(debitP.getMoneyOwnd() + balance);
//            creditP.setMoneyOwnd(creditP.getMoneyOwnd() - balance);
//
//            if (debitP.getMoneyOwnd() > -0.05)
//                i++;
//            if (creditP.getMoneyOwnd() < 0.05)
//                j++;
//        }
//        return debtCollection;
//    }
//
//    public SplitResult getResult(List<Person> group) {
//        var sum = getSum(group);
//        var portion = getPortionSize(group, sum);
//        setMoneyOwned(group, portion);
//        setPersonCategoryList(group);
//        var transactionList = getDebtCollect();
//        return new SplitResult(sum, portion, transactionList);
//    }
//
//}
