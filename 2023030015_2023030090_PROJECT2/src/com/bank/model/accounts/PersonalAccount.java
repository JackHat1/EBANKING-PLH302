package com.bank.model.accounts;

import com.bank.model.users.Individual;

import java.util.ArrayList;
import java.util.List;

public class PersonalAccount extends Account {
    private List<Individual> coOwners = new ArrayList<>();
    private List<String> coOwnerVats = new ArrayList<>(); 

    public PersonalAccount(Individual owner, double interestRate) {
        super(owner, interestRate);
    }

    public void addCoOwner(Individual coOwner) {
        coOwners.add(coOwner);
    }

    public List<Individual> getCoOwners() {
        return coOwners;
    }

    public List<String> getCoOwnerVats() {
        return coOwnerVats;
    }

    @Override
    public String getAccountTypeCode() {
        return "100";
    }

    @Override
    public String marshal() {
        StringBuilder sb = new StringBuilder("type:PersonalAccount"
            + ",iban:" + iban
            + ",primaryOwner:" + owner.getVat()
            + ",dateCreated:" + dateCreated
            + ",rate:" + interestRate
            + ",balance:" + balance);
        for (int i = 0; i < coOwners.size(); i++) {
                sb.append(",coOwner:").append(coOwners.get(i).getVat());
            }
            return sb.toString();
    }

    @Override
    public void unmarshal(String data) {
        super.unmarshal(data);
    
        String[] parts = data.split(",");
        for (int i = 0; i < parts.length; i++) {
            String[] kv = parts[i].split(":", 2);
            if (kv.length < 2) continue;
            if (kv[0].equals("coOwner")) {
                coOwnerVats.add(kv[1]);
            }
        }
    }
}
