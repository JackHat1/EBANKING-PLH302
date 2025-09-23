package com.tuc.ebanking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {
    private final String id = UUID.randomUUID().toString();
    private String iban;
    private BigDecimal balance = BigDecimal.ZERO;
    public String getId() { return id; }
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
