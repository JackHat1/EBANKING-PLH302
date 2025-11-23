package com.bank.model.accounts;

import com.bank.model.users.Customer;
import com.bank.storage.Storable;
import com.bank.utilities.GlobalClock;

import java.time.LocalDate;
import java.util.Random;

public abstract class Account implements Storable {
    protected String iban;
    protected Customer owner;
    protected double balance;
    protected double interestRate;
    protected LocalDate dateCreated;

    public Account(Customer owner, double interestRate) {
        this.iban = generateIBAN(getAccountTypeCode());
        this.owner = owner;
        this.interestRate = interestRate;
        this.balance = 0.0;
        this.dateCreated = GlobalClock.getDate();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
    }   


    public abstract String getAccountTypeCode();

    @Override
    public String marshal() {
        return "type:" + getAccountTypeCode() +",iban:" + iban +",primaryOwner:" + owner.getVat() +",dateCreated:" + dateCreated +",rate:" + interestRate +",balance:" + balance;  
     }

    @Override
    public void unmarshal(String data) {
        String[] parts = data.split(",");
        for (int i = 0; i < parts.length; i++) {
            String[] kv = parts[i].split(":", 2);
            if (kv.length == 2) {
                if (kv[0].equals("iban")) iban = kv[1];
                else if (kv[0].equals("dateCreated")) dateCreated = LocalDate.parse(kv[1]);
                else if (kv[0].equals("rate")) interestRate = Double.parseDouble(kv[1]);
                else if (kv[0].equals("balance")) balance = Double.parseDouble(kv[1]);
            }
        }
    }


        public static String generateIBAN(String typeCode) {
        String country = "GR";
        Random rand = new Random();

        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            accountNumber.append(rand.nextInt(10));
        }

        return country + typeCode + accountNumber;
    }
}