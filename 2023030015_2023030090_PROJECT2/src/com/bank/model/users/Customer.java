package com.bank.model.users;

public abstract class Customer extends User {
    protected String vat; 

    public Customer(String username, String password, String fullName, String vat) {
        super(username, password, fullName);
        this.vat = vat;
    }

    public String getVat() { 
        return vat; }
}
