package com.bank.cli;

import com.bank.manager.*;
import com.bank.model.users.User;
import com.bank.model.accounts.Account;
import com.bank.model.transactions.*;
import com.bank.model.bills.Bill;
import com.bank.model.statements.StatementEntry;

import java.util.*;

public class IndividualMenu {
    private final User user;
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    private final BillManager billManager;
    private final StatementManager statementManager;
    private final Scanner scanner;

    public IndividualMenu(User user, AccountManager accountManager,TransactionManager transactionManager, BillManager billManager, StatementManager statementManager, Scanner scanner) {
        this.user = user;
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
        this.billManager = billManager;
        this.statementManager = statementManager;
        this.scanner = scanner;
    }

    public void run() {            
        while (true) {
            System.out.println("\nSelect an operation:");
            System.out.println("1. View Accounts");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Pay Bill");
            System.out.println("6. View Statements");
            System.out.println("0. Exit");

            String option = scanner.nextLine();

            switch (option) {
                case "1": showAccounts();
                 break;
                case "2": deposit();
                 break;
                case "3": withdraw();
                 break;
                case "4": transfer();
                 break;
                case "5": payBill();
                 break;
                case "6": showStatements();
                 break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private List<Account> getUserAccounts() {
        List<Account> list = new ArrayList<>();
        List<Account> allAccounts = accountManager.getAllAccounts();
        for (int i = 0; i < allAccounts.size(); i++) {
            Account acc = allAccounts.get(i);

            if (acc.getOwner().equals(user)) {
                list.add(acc);
            }
        }
        return list;
    }

    private Account selectAccount(List<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++){
            System.out.println((i + 1) + ". " + accounts.get(i).getIban());
        }

        System.out.print("Select account: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < accounts.size()){
            return accounts.get(choice);
        }

        return null;
    }

    private void showAccounts() {
        List<Account> accounts = getUserAccounts();
        for (int i = 0; i < accounts.size(); i++) {

            Account acc = accounts.get(i);
            System.out.println("- " + acc.getIban() + " | Balance: " + acc.getBalance());
        }
    }

    private void deposit() {
        List<Account> accounts = getUserAccounts();
        Account account = selectAccount(accounts);

        if (account == null){
             return;
        }

        System.out.print("Deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        transactionManager.execute(new Deposit(account, amount, user, "Deposit via CLI"));

        accountManager.saveAll();
    }


    private void withdraw() {
        List<Account> accounts = getUserAccounts();
        Account account = selectAccount(accounts);

        if (account == null) {
            return;
        }

        System.out.print("Withdrawal amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (amount > account.getBalance()) {
            System.out.println("Insufficient balance.");
            return;
        }

        transactionManager.execute(new Withdrawal(account, amount, user, "Withdrawal via CLI"));
        accountManager.saveAll();
    }


    private void transfer() {
        List<Account> myAccounts = getUserAccounts();
        Account from = selectAccount(myAccounts);
        if (from == null){
            return;
        }

        System.out.print("Recipient IBAN: ");
        String toIban = scanner.nextLine();
        Account to = accountManager.findByIban(toIban);

        if (to == null || from.getIban().equals(toIban)) {
            System.out.println("Invalid IBAN.");
            return;
        }
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Reason: ");
        String reason = scanner.nextLine();
        transactionManager.execute(new Transfer(from, to, amount, user, reason, reason));
        accountManager.saveAll();

    }


    private void payBill() {

        List<Account> accounts = getUserAccounts();
        Account from = selectAccount(accounts);
        
        if (from == null) {
            return;
        }

        System.out.print("Bill RF: ");
        String rf = scanner.nextLine();

        Bill bill = billManager.getBillByRF(rf);
        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }

        if (bill.isPaid()) {
            System.out.println("Bill is already paid.");
            return;
        }

        double amount = bill.getAmount();
        System.out.println("Bill amount: " + amount);

        if (from.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        Account business = null;
        List<Account> allAccounts = accountManager.getAllAccounts();
        for (Account acc : allAccounts) {
            if (!acc.getOwner().equals(user)) {
                business = acc;
                break;
            }
        }

        if (business == null) {
            System.out.println("No business account found.");
            return;
        }

     
        transactionManager.execute(new Payment(bill, from, business, user));
        bill.setPaid(true);

        billManager.saveAll();
        accountManager.saveAll();             

        System.out.println("Bill payment successful.");
    }



    private void showStatements() {
        List<Account> accounts = getUserAccounts();
        Account account = selectAccount(accounts);
        if (account == null) {
            return;
        }
        
        List<StatementEntry> entries = statementManager.load(account);
        if (entries.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(entries.get(i));
        }
}
}