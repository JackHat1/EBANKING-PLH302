package com.bank.model.statements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.bank.storage.Storable;

public class StatementEntry implements Storable{

    private String transactor;
    private String reason;
    private String type; 
    private double amount;
    private double balance;
    private LocalDateTime timestamp;
    private String fromIban;
    private String toIban;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatementEntry(String transactor, String fromIban, String toIban, double amount, String reason, String type, LocalDateTime timestamp, double balance) {
        this.transactor = transactor;
        this.fromIban = fromIban;
        this.toIban = toIban;
        this.amount = amount;
        this.reason = reason;
        this.type = type;
        this.timestamp = timestamp;
        this.balance = balance;
    }

    public StatementEntry(){
        
    }

    public String getTransactor() { return transactor; }
    public String getReason() { return reason; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getFromIban() { return fromIban; }
    public String getToIban() { return toIban; }

    public String marshal() {
        return String.join(",",
            transactor,
            fromIban,
            toIban,
            String.valueOf(amount),
            reason.replace(",", " "),
            type,
            formatter.format(timestamp),
            String.valueOf(balance)
        );
    }

    public void unmarshal(String data) {
        try {
            String[] parts = data.split(",", -1);
            this.transactor = parts[0];
            this.fromIban = parts[1];
            this.toIban = parts[2];
            this.amount = Double.parseDouble(parts[3]);
            this.reason = parts[4];
            this.type = parts[5];
            this.timestamp = LocalDateTime.parse(parts[6], formatter);
            this.balance = Double.parseDouble(parts[7]);

        } catch (Exception e) {
            System.err.println("Error unmarshal StatementEntry: " + e.getMessage());
           
        }
    }

    
    @Override
    public String toString() {
        return "Statement: " +
               "Transactor: " + transactor +
               ", From Iban: " + fromIban +
               ", To Iban: " + toIban +
               ", Amount: " + amount +
               ", Reason: " + reason +
               ", Type(debit or credit): " + type +
               ", Time: " + formatter.format(timestamp) +
               ", Remaining Balance: " + balance;
    }
}
