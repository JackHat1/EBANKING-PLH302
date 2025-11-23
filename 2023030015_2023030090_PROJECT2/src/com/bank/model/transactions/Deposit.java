package com.bank.model.transactions;

import com.bank.model.users.User;

import com.bank.manager.StatementManager;
import com.bank.model.accounts.Account;
import com.bank.model.statements.StatementEntry;

public class Deposit extends Transaction {
    private Account account;
    private double amount;

    public Deposit(Account account, double amount, User transactor, String reason) {
        super(transactor, "Deposit");
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
    
        account.deposit(amount);
        System.out.println("Deposit of " + amount + "€ in " + account.getIban());

        StatementEntry entry = new StatementEntry(
            getTransactor().getUsername(),
            null,
            account.getIban(),
            amount,
            reason,
            "Credit",
            this.timestamp,
            account.getBalance()
        );
        
        StatementManager statementManager = new StatementManager();
        statementManager.saveStatement(account, entry);
    }


        public Account getAccount() {
        return account;
    }

        public double getAmount() {
            return amount;
        }
}
