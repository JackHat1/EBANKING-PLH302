package com.bank.model.users;

public class Admin extends User {
    public Admin() {
        super("", "", "");
    }

    public Admin(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String marshal() {
        return "type:Admin"
             + ",legalName:" + fullName
             + ",userName:" + username
             + ",password:" + password;
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
            }
        }
    }
}
