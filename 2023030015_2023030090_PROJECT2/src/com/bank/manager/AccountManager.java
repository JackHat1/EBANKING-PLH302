package com.bank.manager;

import com.bank.model.accounts.*;
import com.bank.model.users.*;
import com.bank.storage.CsvStorageManager;
import com.bank.storage.Storable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private List<Account> accounts = new ArrayList<>();
    private final String path = "./data/accounts/accounts.csv";
    private CsvStorageManager storage = new CsvStorageManager();
    private UserManager userManager;

    public AccountManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void load() {
        List<String> lines = storage.loadLines(path);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            String type = "", iban = "", vat = "";
            double balance = 0, interest = 0, fee = 0;
            LocalDate dateCreated = null;

            for (int j = 0; j < fields.length; j++) {
                String[] parts = fields[j].split(":", 2);
                if (parts.length < 2) continue;

                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.equals("type")) type = value.toUpperCase();
                else if (key.equals("iban")) iban = value;
                else if (key.equals("primaryOwner")) vat = value;
                else if (key.equals("balance")) balance = Double.parseDouble(value);
                else if (key.equals("rate")) interest = Double.parseDouble(value);
                else if (key.equals("fee")) fee = Double.parseDouble(value);
                else if (key.equals("dateCreated")) dateCreated = LocalDate.parse(value);
            }

            Customer owner = userManager.findByVat(vat);
            Account acc = null;

            if (type.equals("PERSONALACCOUNT") && owner instanceof Individual) {
                acc = new PersonalAccount((Individual) owner, interest);
            } else if (type.equals("BUSINESSACCOUNT") && owner instanceof Company) {
                acc = new BusinessAccount((Company) owner, interest, fee);
            }

            if (acc != null) {
                acc.setIban(iban);
                acc.setBalance(balance);
                if (dateCreated != null) acc.setDateCreated(dateCreated);
                acc.unmarshal(line);

                accounts.add(acc);

                if (acc instanceof PersonalAccount) {
                    PersonalAccount pa = (PersonalAccount) acc;
                    List<String> coOwnerVats = pa.getCoOwnerVats();
                    for (int k = 0; k < coOwnerVats.size(); k++) {
                        String coVat = coOwnerVats.get(k);
                        Individual co = (Individual) userManager.findByVat(coVat);
                        if (co != null) {
                            pa.addCoOwner(co);
                        }
                    }
                }
            }
        }
    }

    public void saveAll() {
        List<Storable> list = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            list.add(accounts.get(i));
        }
        storage.saveAll(list, path, false);
    }

    public Account findByIban(String iban) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getIban().equals(iban)) {
                return acc;
            }
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
    }
}