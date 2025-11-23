package com.bank.cli;

import com.bank.manager.*;
import com.bank.model.accounts.*;
import com.bank.model.orders.StandingOrder;
import com.bank.model.users.*;
import com.bank.utilities.GlobalClock;
import com.bank.model.bills.Bill;

import java.time.LocalDate;
import java.util.*;

    public class AdminMenu {
        private final UserManager userManager;
        private final AccountManager accountManager;
        private final BillManager billManager;
        private final TransactionManager transactionManager;
        private final StandingOrderManager orderManager;
        private final Scanner scanner;

        public AdminMenu(UserManager userManager, AccountManager accountManager, BillManager billManager,
                        TransactionManager transactionManager, StandingOrderManager orderManager, Scanner scanner) {
            this.userManager = userManager;
            this.accountManager = accountManager;
            this.billManager = billManager;
            this.transactionManager = transactionManager;
            this.orderManager = orderManager;
            this.scanner = scanner;
        }



    public void run() {
        while (true) {
            System.out.println("1. Show Customers");
            System.out.println("2. Show Customer Details");
            System.out.println("3. Show Bank Accounts");
            System.out.println("4. Show Bank Account Info");
            System.out.println("5. Show Bank Account Statements");
            System.out.println("6. Show Issued Bills");
            System.out.println("7. Show Paid Bills");
            System.out.println("8. Load Company Bills");
            System.out.println("9. List Standing Orders");
            System.out.println("10. Pay Customer's Bill");
            System.out.println("11. Simulate Time Passing");
            System.out.println("0. Exit");

            System.out.print("\nSelect option: ");
            String option = scanner.nextLine();

        switch (option) {
            case "1": showUsers(); 
            break;
            case "2": showCustomerDetails(); 
            break;
            case "3": showAccounts(); 
            break;
            case "4": showAccountInfo(); 
            break;
            case "5": showAccountStatements(); 
            break;
            case "6": showIssuedBills(); 
            break;
            case "7": showPaidBills(); 
            break;
            case "8": loadCompanyBills(); 
            break;
            case "9": listOrders(); 
            break;
            case "10":payCustomerBill(); 
            break;
            case "11":simulateTime(); 
            break;
            case "0": 
            return;
            default: System.out.println("Invalid option./n");
            }


        }
    }


    private void showUsers() {
        List<User> users = userManager.getAllUsers();

        System.out.println("\nUsers:");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.print("\n- " + u.getUsername() + " (" + u.getFullName() + ") | Role: " + u.getRole());
        }
    }


    private void showCustomerDetails() {
        System.out.print("Enter VAT of the customer: ");
        String vat = scanner.nextLine();
        Customer customer = userManager.findByVat(vat);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Name: " + customer.getFullName());
        System.out.println("Username: " + customer.getUsername());
        System.out.println("VAT: " + customer.getVat());
        System.out.println("Role: " + customer.getRole());

        List<Account> accounts = accountManager.getAllAccounts();

        System.out.println("Accounts:");
        for (Account acc : accounts) {
            if (acc.getOwner().equals(customer)) {
                System.out.println(" - " + acc.getIban() + " | Balance: " + acc.getBalance());
            }
        }
    }


    private void showAccounts() {
        List<Account> accounts = accountManager.getAllAccounts();
        System.out.println("\nAccounts:");

        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.println("- " + a.getIban() + " | Owner: " + a.getOwner().getFullName() + " | Balance: " + a.getBalance());
        }
    }


    private void showAccountInfo() {
    System.out.print("Enter IBAN of the account: ");
    String iban = scanner.nextLine();

    Account acc = accountManager.findByIban(iban);

    if (acc == null) {
        System.out.println("Account not found.");
        return;
    }

    System.out.println("\nAccount Information:");
    System.out.println("- IBAN: " + acc.getIban());
    System.out.println("- Owner: " + acc.getOwner().getFullName() + " (" + acc.getOwner().getVat() + ")");
    System.out.println("- Balance: " + acc.getBalance());
    System.out.println("- Interest Rate: " + acc.getInterestRate());
    System.out.println("- Created On: " + acc.getDateCreated());

    if (acc instanceof BusinessAccount) {

        System.out.println("- Type: Business Account");
        System.out.println("- Monthly Fee: " + ((BusinessAccount) acc).getMonthlyFee());
    } else if (acc instanceof PersonalAccount) {
        
        System.out.println("- Type: Personal Account");
        List<Individual> coOwners = ((PersonalAccount) acc).getCoOwners();

        if (coOwners.isEmpty()) {
            System.out.println("- No co-owners.");
        } else {
            System.out.println("- Co-owners:");
            for (Individual co : coOwners) {
                System.out.println("  * " + co.getFullName() + " (" + co.getVat() + ")");
            }
        }
    } else {
        System.out.println("- Type: Unknown");
    }
    }


    private void showAccountStatements() {
        System.out.print("Enter IBAN of account: ");
        String iban = scanner.nextLine();
        Account account = accountManager.findByIban(iban);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        List<com.bank.model.statements.StatementEntry> entries = new StatementManager().load(account);

        if (entries.isEmpty()) {
            System.out.println("No statement entries found.");
        } else {
            for (com.bank.model.statements.StatementEntry entry : entries) {
                System.out.println(entry);
            }
        }
    }


    private void createUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter role (Individual / Company / Admin): ");
        String role = scanner.nextLine().trim().toLowerCase();

        User newUser = null;

        if (role.equals("individual") || role.equals("company")) {
            System.out.print("Enter VAT number: ");
            String vat = scanner.nextLine();

            if (role.equals("individual")) {
                newUser = new Individual(username, password, fullName, vat);
            } else {
                newUser = new Company(username, password, fullName, vat);
            }
        
        } else if (role.equals("admin")) {
            newUser = new Admin(username, password, fullName);
        } else {
            System.out.println("Invalid role.");
            return;
        }

        userManager.addUser(newUser);
        System.out.println("User created successfully.");
    }


    private void createAccount() {
        System.out.print("Enter VAT of account owner: ");
        String vat = scanner.nextLine();
        Customer customer = userManager.findByVat(vat);

        if (customer == null) {
            System.out.println("Customer with VAT " + vat + " not found.");
            return;
        }

        System.out.print("Enter interest rate (e.g. 0.02): ");
        double rate;
        try {
            rate = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate.");
            return;
        }

        Account newAccount = null;

        if (customer instanceof Individual) {
            newAccount = new PersonalAccount((Individual) customer, rate);

        } else if (customer instanceof Company) {
            System.out.print("Enter monthly fee: ");
            double fee;

            try {
                fee = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid fee.");
                return;
            }
            newAccount = new BusinessAccount((Company) customer, rate, fee);
        }

        if (newAccount != null) {
            accountManager.addAccount(newAccount);
            System.out.println("Account created with IBAN: " + newAccount.getIban());
        }

    }


    private void showIssuedBills() {
        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();

        System.out.print("Enter company VAT: ");
        String vat = scanner.nextLine();
        Customer company = userManager.findByVat(vat);

        System.out.println("Issued Bills:");
        for (Bill bill : billManager.getAllBills()) {
            if (bill.getIssuer() != null &&

                bill.getIssuer().getOwner().equals(company)) {
                System.out.println(bill);
            }

        }


    }


    private void showPaidBills() {
        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();

        System.out.print("Enter company VAT: ");
        String vat = scanner.nextLine();
        Customer company = userManager.findByVat(vat);

        System.out.println("Paid Bills:");
        for (Bill bill : billManager.getAllBills()) {
            if (bill.getIssuer() != null &&

                bill.getIssuer().getOwner().equals(company) &&
                bill.isPaid()) {
                System.out.println(bill);
            }

        }


    }


    private void loadCompanyBills() {
        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();
        System.out.println("Company bills loaded successfully.");

    }


    private void payCustomerBill() {
        System.out.print("Enter customer's VAT: ");
        String vat = scanner.nextLine();
        Customer customer = userManager.findByVat(vat);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        List<Account> accounts = accountManager.getAllAccounts();
        List<Account> owned = new ArrayList<>();

        for (Account acc : accounts) {
            if (acc.getOwner().equals(customer)) {
                owned.add(acc);
            }
        }

        if (owned.isEmpty()) {
            System.out.println("Customer has no accounts.");
            return;
        }

        for (int i = 0; i < owned.size(); i++) {
            System.out.println((i + 1) + ". " + owned.get(i).getIban() + " | Balance: " + owned.get(i).getBalance());
        }

        System.out.print("Select account: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= owned.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Account from = owned.get(choice);

        System.out.print("Enter RF code of the bill: ");
        String rf = scanner.nextLine();

        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();

        Bill bill = billManager.getBillByRF(rf);
        if (bill == null || bill.isPaid()) {
            System.out.println("Invalid or already paid bill.");
            return;
        }

        TransactionManager tm = new TransactionManager();
        tm.execute(new com.bank.model.transactions.Payment(bill, from, bill.getIssuer(), customer));
        bill.setPaid(true);
        billManager.saveAll();

        accountManager.saveAll();


        System.out.println("Bill paid successfully.");
    }


    private void listOrders() {
        StandingOrderManager orderManager = new StandingOrderManager();
        BillManager billMan= new BillManager(accountManager,userManager);
        billMan.loadBills();
        TransactionManager transMan = new TransactionManager();

        orderManager.executeAllOrders(GlobalClock.getDate(), billMan,accountManager, transMan, null);

        List<StandingOrder> orders = orderManager.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No standing orders found.");
            return;
        }

        System.out.println("\nStanding Orders:");
        for (StandingOrder order : orders) {
            System.out.println("- ID: " + order.getOrderId());
            System.out.println("  Title: " + order.getTitle());
            System.out.println("  Description: " + order.getDescription());
            
            boolean isActive= order.getIsActive() && !order.isExpired() && !order.isFailed();
            System.out.println("  Active: " + (order.getIsActive() ? "Yes" : "No"));
            
            System.out.println("  Period: " + order.getStartingDate() + " to " + order.getEndingDate());
            System.out.println("  Expired: " + (order.isExpired() ? "Yes" : "No"));
            System.out.println("  Failed: " + (order.isFailed() ? "Yes" : "No"));
            System.out.println();
        }
    }


    private void simulateTime() {
        System.out.print("Enter target date (YYYY-MM-DD): ");
        String input = scanner.nextLine();
        LocalDate target;
        try {
            target = LocalDate.parse(input);
        } catch (Exception e) {
            System.out.println("Invalid date.");
            return;
        }

        LocalDate today= GlobalClock.getDate(); 
        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();
        StandingOrderManager orderManager = new StandingOrderManager();
        orderManager.loadOrders();
        TransactionManager transactionManager = new TransactionManager();

        while (!today.isAfter(target)) {
            GlobalClock.setDate(today);

            for (StandingOrder order : orderManager.getAllOrders()) {
                User owner = userManager.findByVat(order.getDescription()); 
                if (owner != null)
                    order.execute(today, billManager, accountManager, transactionManager, owner);
            }

            for (Account acc : accountManager.getAllAccounts()) {
                if (today.getDayOfMonth() == 1) {
                    acc.deposit(acc.getBalance() * acc.getInterestRate());
                    if (acc instanceof BusinessAccount) {
                        acc.withdraw(((BusinessAccount) acc).getMonthlyFee());
                    }
                }
            }

            today = today.plusDays(1);
        }

        System.out.println("\nTime simulation completed.");
    }

}