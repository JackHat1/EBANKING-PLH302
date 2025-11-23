package com.bank.manager;

import com.bank.model.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final List<Transaction> history = new ArrayList<>();

    public void execute(Transaction transaction) {
        try {
            transaction.execute();
            history.add(transaction);
        } catch (Exception e) {
            System.err.println("Error at the transaction execution: " + e.getMessage());
        }
    }


    public List<Transaction> getHistory() {
        return new ArrayList<>(history);
    }
}