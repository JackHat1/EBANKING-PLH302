package com.bank.cli;

import com.bank.manager.*;
import com.bank.model.users.*;

import java.util.Scanner;

public class Main {

    private static final UserManager userManager = new UserManager();
    private static final TransactionManager transactionManager = new TransactionManager();
    private static final AccountManager accountManager = new AccountManager(userManager);
    private static final BillManager billManager = new BillManager(accountManager , userManager);
    private static final Scanner scanner = new Scanner(System.in);
     private static User loggedInUser;

    public static void main(String[] args) {
        userManager.load();
        accountManager.load();
        billManager.loadBills();
        
        login();

        if (loggedInUser instanceof Individual) {
            new IndividualMenu(loggedInUser, accountManager, transactionManager, billManager, new StatementManager(), scanner).run();
        } else if (loggedInUser instanceof Company) {
            new CompanyMenu(loggedInUser, accountManager, billManager, scanner).run();
        } else if (loggedInUser instanceof Admin) {
            new AdminMenu(userManager, accountManager, billManager, transactionManager, new StandingOrderManager(), scanner).run();
        } else {
            System.out.println("User type not supported.");
        }

        saveAndExit();
    }

    private static void login() {
        System.out.println("User Login");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userManager.findByUsername(username);
        if (user != null && user.checkPassword(password)) {
            loggedInUser = user;
            System.out.println("Welcome " + user.getFullName());
        } else {
            System.out.println("Invalid credentials.");
            System.exit(0);
        }
    }

    private static void saveAndExit() {
        System.out.println("Saving data...");
        userManager.saveAll();
        accountManager.saveAll();
        System.out.println("Save complete. Goodbye!");
    }
}