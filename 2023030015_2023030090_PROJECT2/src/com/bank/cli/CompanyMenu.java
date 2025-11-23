package com.bank.cli;

import com.bank.manager.*;
import com.bank.model.users.User;
import com.bank.model.accounts.Account;
import com.bank.model.bills.Bill;

import java.util.*;

public class CompanyMenu {
    private final User user;
    private final AccountManager accountManager;
    private final BillManager billManager;
    private final Scanner scanner;

    public CompanyMenu(User user, AccountManager accountManager, BillManager billManager, Scanner scanner) {
        this.user = user;
        this.accountManager = accountManager;
        this.billManager = billManager;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\nCompany Menu:");
            System.out.println("1. View Accounts");
            System.out.println("2. Create New Bill");
            System.out.println("3. View All Issued Bills");
            System.out.println("4. View Paid Bills");
            System.out.println("0. Exit");

            System.out.print("Select option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": showAccounts(); break;
                case "2": createBill(); break;
                case "3": showIssued(); break;
                case "4": showPaid(); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void showAccounts() {
        List<Account> accounts = accountManager.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);

            if (acc.getOwner().equals(user)) {
                System.out.println("- IBAN: " + acc.getIban() + " | Balance: " + acc.getBalance());
            }
        }
    }

    private void createBill() {
        Account companyAccount = null;
        List<Account> allAccounts = accountManager.getAllAccounts();
        for (int i = 0; i < allAccounts.size(); i++) {
            Account acc = allAccounts.get(i);

            if (acc.getOwner().equals(user)) {
                companyAccount = acc;
                break;
            }
        }

        if (companyAccount == null) {
            System.out.println("No account found for this company.");
            return;
        }

        System.out.print("Enter customer VAT: ");
        String customerVat = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter RF code: ");
        String rf = scanner.nextLine();
        System.out.print("Enter bill number: ");
        String billNo = scanner.nextLine();

        Bill bill = new Bill(billNo, rf, amount, companyAccount);
        billManager.createBill(bill);
        billManager.saveAll();
        System.out.println("Bill issued successfully.");
    }

    private void showIssued() {
        System.out.println("Issued Bills:");
        List<Bill> allBills = billManager.getAllBills();
        for (int i = 0; i < allBills.size(); i++) {
            Bill bill = allBills.get(i);

            if (bill.getIssuer() != null && bill.getIssuer().getOwner().equals(user)) {
                System.out.println(bill);
            }
        }
        accountManager.saveAll();
        billManager.saveAll();
    }

    private void showPaid() {
        System.out.println("Paid Bills:");
        List<Bill> allBills = billManager.getAllBills();
        
        for (int i = 0; i < allBills.size(); i++) {
            Bill bill = allBills.get(i);

            if (bill.getIssuer() != null && bill.getIssuer().getOwner().equals(user) && bill.isPaid()) {
                System.out.println(bill);
            }
        }
    }
}