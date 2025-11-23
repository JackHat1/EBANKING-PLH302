package com.bank.model.users;

public class Individual extends Customer {
    public Individual() {
        super("", "", "", "");
    }

    public Individual(String username, String password, String fullName, String vat) {
        super(username, password, fullName, vat);
    }

    @Override
    public String getRole() {
        return "Individual";
    }

    @Override
    public String marshal() {
        return "type:Individual"
             + ",legalName:" + fullName
             + ",userName:" + username
             + ",password:" + password
             + ",vatNumber:" + vat;
    }

    @Override
    public void unmarshal(String line) {
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) {
            String[] kv = parts[i].split(":");
            if (kv.length == 2) {
                if (kv[0].equals("legalName")) fullName = kv[1];
                else if (kv[0].equals("userName")) username = kv[1];
                else if (kv[0].equals("password")) password = kv[1];
                else if (kv[0].equals("vatNumber")) vat = kv[1];
            }
        }
    }
}
