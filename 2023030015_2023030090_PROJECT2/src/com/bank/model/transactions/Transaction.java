package com.bank.model.transactions;

import com.bank.model.statements.StatementEntry;
import com.bank.model.users.User;
import com.bank.utilities.GlobalClock;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaction    {
    protected StatementEntry statementEntry;
    protected String transactionId;
    protected LocalDateTime timestamp;
    protected User transactor;
    protected String reason;
    

    public Transaction(User transactor, String reason) {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = GlobalClock.getDateTime();
        this.transactor = transactor;
        this.reason = reason;
    }

    public StatementEntry getStatementEntry(){
        return statementEntry;
    }

    public String getTransactionId() { 
        return transactionId; 
    }

    public LocalDateTime getTimestamp() {
         return timestamp; 
    }

    public User getTransactor() {
         return transactor; 
    }

    public String getReason() {
         return reason; 
    }


    public abstract void execute();

    


}
