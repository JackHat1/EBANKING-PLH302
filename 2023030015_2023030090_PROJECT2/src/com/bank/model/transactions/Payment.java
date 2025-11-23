package com.bank.model.transactions;

import com.bank.manager.StatementManager;
import com.bank.model.accounts.Account;
import com.bank.model.users.User;
import com.bank.model.bills.Bill;
import com.bank.model.statements.StatementEntry;

public class Payment extends Transaction {
    private Bill bill;
    private Account from;
    private Account business;

    public Payment(Bill bill, Account from, Account business, User transactor) {
        super(transactor, "Payment");
        this.bill = bill;
        this.from = from;
        this.business = business;
    }

    public Bill getBill() {
        return bill;
    }

    @Override
    public void execute() {
        if (from.getBalance() >= bill.getAmount()) {
            from.withdraw(bill.getAmount());
            business.deposit(bill.getAmount());
            bill.setPaid(true);

            System.out.println("Payment of " + bill.getAmount() + "€ to business " + business.getIban() + " [RF: " + bill.getPaymentCode() + "]");

            StatementEntry fromAccountEntry = new StatementEntry(getTransactor().getUsername(),from.getIban(),business.getIban(),bill.getAmount(),"Payment [" + bill.getPaymentCode() + "]","Debit", this.timestamp, from.getBalance());

            StatementEntry businessAccountEntry = new StatementEntry( getTransactor().getUsername(),from.getIban(),business.getIban(),bill.getAmount(),"Receive Payment [" + bill.getPaymentCode() + "]","Credit", this.timestamp, business.getBalance());

            StatementManager statementManager = new StatementManager();
            statementManager.saveStatement(from, fromAccountEntry);
            statementManager.saveStatement(business, businessAccountEntry);

        } 
        else {
             System.out.println("Unavailable payment due to insufficient balance.");
        }
    }

    public Account getFrom() { 
        return from; 
    }

    public Account getBusiness() { 
        return business; 
    }

}
