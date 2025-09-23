package com.tuc.ebanking.service;

import com.tuc.ebanking.model.Account;
import java.math.BigDecimal;

public class TransferService {
    public void transfer(Account from, Account to, BigDecimal amount){
        if (amount.signum() <= 0) throw new IllegalArgumentException("amount <= 0");
        if (from.getBalance().compareTo(amount) < 0) throw new IllegalStateException("insufficient funds");
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }
}
