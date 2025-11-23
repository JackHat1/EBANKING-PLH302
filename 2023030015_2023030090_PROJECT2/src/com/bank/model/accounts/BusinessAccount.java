package com.bank.model.accounts;

import com.bank.model.users.Company;

public class BusinessAccount extends Account {
    private double monthlyFee;

    public BusinessAccount(Company owner, double interestRate, double monthlyFee) {
        super(owner, interestRate);
        this.monthlyFee = monthlyFee;
    }

    public double getMonthlyFee(){
    return monthlyFee;
    }

    @Override
    public String getAccountTypeCode() {
        return "200";
    }
    
    @Override
    public String marshal() {
        return "type:BusinessAccount,iban:" + getIban() + ",primaryOwner:" + getOwner().getVat() + ",dateCreated:" + dateCreated + ",rate:" + getInterestRate() + ",balance:" + getBalance() + ",fee:" + getMonthlyFee();
    }

    @Override
    public void unmarshal(String data) {
        super.unmarshal(data); 

        String[] parts = data.split(",");
        for (int i = 0; i < parts.length; i++) {
            String[] kv = parts[i].split(":", 2);
            if (kv.length == 2) {
                if (kv[0].equals("fee")) {
                    this.monthlyFee = Double.parseDouble(kv[1]);
                }
            }
        }
    }

    
    

}
