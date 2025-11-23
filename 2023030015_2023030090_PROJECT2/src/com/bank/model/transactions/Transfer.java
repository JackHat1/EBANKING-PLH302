package com.bank.model.transactions;

import com.bank.manager.StatementManager;
import com.bank.model.accounts.Account;
import com.bank.model.statements.StatementEntry;
import com.bank.model.users.User;

public class Transfer extends Transaction {
    private Account from;
    private Account to;
    private double amount;
    private String senderReason;
    private String receiverReason;

    public Transfer(Account from, Account to, double amount, User transactor, String senderReason, String receiverReason) {
        super(transactor, "Transfer");
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.senderReason = senderReason;
        this.receiverReason = receiverReason;
    }

    @Override
    public void execute() {
        if (from.getBalance() >= amount) {
            from.withdraw(amount);
            to.deposit(amount);

            System.out.println("Transfer of " + amount + "€ from " + from.getIban() + " to " + to.getIban());
            System.out.println("Sender Reason: " + senderReason);
            System.out.println("Receiver Reason: " + receiverReason);

            StatementEntry fromAccountEntry = new StatementEntry(getTransactor().getUsername(),from.getIban(),to.getIban(),amount,senderReason,"Debit", this.timestamp, from.getBalance());

            StatementEntry toAccountEntry = new StatementEntry(getTransactor().getUsername(),from.getIban(),to.getIban(),amount,receiverReason,"Credit", this.timestamp, to.getBalance());

            StatementManager statementManager = new StatementManager();
            statementManager.saveStatement(from, fromAccountEntry);
            statementManager.saveStatement(to, toAccountEntry);

        } else {
            System.out.println("Unavailable transfer due to insufficient balance.");
        }
    }

    public Account getFrom() { 
        return from; 
    }
    public Account getTo() {
         return to; 
    }
    public double getAmount() {
         return amount;
    }
}