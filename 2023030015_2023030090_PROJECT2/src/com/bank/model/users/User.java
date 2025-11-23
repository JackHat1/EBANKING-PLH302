package com.bank.model.users;

import com.bank.storage.Storable;

public abstract class User implements Storable {
    protected String username;
    protected String password;
    protected String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public abstract String getRole();

    public String getUsername() {
         return username; 
    }
    public String getPassword() {
         return password; 
    }
    public String getFullName() {
         return fullName;
    }

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }


}
