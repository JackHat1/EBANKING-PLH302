package com.tuc.ebanking.dao;

import com.tuc.ebanking.model.Account;
import java.util.*;

public class AccountDao {
    private final Map<String, Account> byId = new HashMap<>();
    public void save(Account a){ byId.put(a.getId(), a); }
    public Optional<Account> findById(String id){ return Optional.ofNullable(byId.get(id)); }
    public Collection<Account> findAll(){ return byId.values(); }
}
