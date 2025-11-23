package com.bank.model.orders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.bank.manager.AccountManager;
import com.bank.manager.BillManager;
import com.bank.manager.TransactionManager;
import com.bank.model.users.User;
import com.bank.storage.Storable;
import com.bank.utilities.GlobalClock;

public abstract class StandingOrder implements Storable {
    
    protected String orderId;
    protected String title;
    public String description;
    public Boolean isActive = true;
    protected LocalDate startingDate;
    protected LocalDate endingDate;

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public StandingOrder(String title, String description, LocalDate startingDate, LocalDate endingDate){
        this.orderId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.startingDate = startingDate;
        this.endingDate = endingDate;

    }

    
    public String getOrderId() {
        return orderId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }


    public abstract void execute(LocalDate date, BillManager billMan, AccountManager accountMan, TransactionManager transMan, User user);

    public abstract String marshal();
    public abstract void unmarshal(String data);


    public boolean isExpired(){
        return GlobalClock.getDate().isAfter(getEndingDate());
    }

    public boolean isFailed(){
        return false;
    }


}
