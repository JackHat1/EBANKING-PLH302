package com.bank.manager;

import com.bank.model.users.*;
import com.bank.storage.CsvStorageManager;
import com.bank.storage.Storable;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users = new ArrayList<>();
    private CsvStorageManager storage = new CsvStorageManager();

    private final String path = "./data/users/users.csv";

    public void load() {
        List<String> lines = storage.loadLines(path);
    
        for (String line : lines) {
            String[] fields = line.split(",");
            String role = "", username = "", password = "", fullName = "", vat = "";
    
            for (String field : fields) {
                String[] parts = field.split(":");
                if (parts.length < 2) {
                    continue;
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
    
                switch (key) {
                    case "type": role = value.toUpperCase(); break;
                    case "userName": username = value; break;
                    case "password": password = value; break;
                    case "legalName": fullName = value; break;
                    case "vatNumber": vat = value; break;
                }
            }
    
            User user = null;
    
            if (role.equals("INDIVIDUAL")) {
                user = new Individual(username, password, fullName, vat);
            } else if (role.equals("COMPANY")) {
                user = new Company(username, password, fullName, vat);
            } else if (role.equals("ADMIN")) {
                user = new Admin(username, password, fullName);
            }
    
            if (user != null) {
                users.add(user);
            }
        }
        
    }
    

    public void saveAll() {
        List<Storable> list = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            list.add(users.get(i));
        }
        storage.saveAll(list, path, false);
    }

    public void addUser(User user) {
        if (findByUsername(user.getUsername()) != null) {
            System.out.println("Username already exists: " + user.getUsername());
            return;
        }
        users.add(user);
    }

    public User findByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }

    public Customer findByVat(String vat) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u instanceof Customer && ((Customer) u).getVat().equals(vat)) {
                return (Customer) u;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return users;
    }
}
