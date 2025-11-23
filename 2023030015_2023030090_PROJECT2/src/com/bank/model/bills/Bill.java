package com.bank.model.bills;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.bank.storage.Storable;
import com.bank.utilities.GlobalClock;
import com.bank.model.accounts.Account;

public class Bill implements Storable {
    private String paymentCode;
    private String billNumber;
    private double amount;
    private Account issuer;       
    private String issuerVat;     
    private String customerVat;   
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean isPaid;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Bill(String billNumber, String paymentCode, double amount, Account issuer) {
        this.billNumber = billNumber;
        this.paymentCode = paymentCode;
        this.amount = amount;
        this.issuer = issuer;
        this.issueDate = GlobalClock.getDate();
        this.dueDate = GlobalClock.getDate().plusDays(30);
    }


    public String getPaymentCode() { return paymentCode; }
    public String getBillNumber() { return billNumber; }
    public double getAmount() { return amount; }
    public Account getIssuer() { return issuer; }
    public void setIssuer(Account issuer) { this.issuer = issuer; }
    public String getIssuerVat() { return issuerVat; }
    public String getCustomerVat() { return customerVat; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return "Bill{ " +
                "Payment Code: " + paymentCode +
                ", Bill Number: " + billNumber +
                ", Issuer VAT: " + issuerVat +
                ", Amount: " + amount +
                ", Issue Date: " + issueDate.format(formatter) +
                ", Due Date: " + dueDate.format(formatter) +
                ", Paid: " + isPaid +
                " }";
    }

    @Override
    public String marshal() {
        return "type:Bill" +
                ",paymentCode:" + paymentCode +
                ",billNumber:" + billNumber +
                ",issuer:" + issuerVat +
                ",customer:" + customerVat +
                ",amount:" + amount +
                ",issueDate:" + issueDate.format(formatter) +
                ",dueDate:" + dueDate.format(formatter) +
                ",isPaid:" + isPaid;
    }

    @Override
    public void unmarshal(String data) {
        String[] parts = data.split(",");
        for (String part : parts) {
            String[] kv = part.split(":", 2);
            if (kv.length != 2) continue;
            switch (kv[0]) {
                case "paymentCode": paymentCode = kv[1]; break;
                case "billNumber": billNumber = kv[1]; break;
                case "issuer": issuerVat = kv[1]; break;
                case "customer": customerVat = kv[1]; break;
                case "amount": amount = Double.parseDouble(kv[1]); break;
                case "issueDate": issueDate = LocalDate.parse(kv[1]); break;
                case "dueDate": dueDate = LocalDate.parse(kv[1]); break;
                case "isPaid": isPaid = Boolean.parseBoolean(kv[1]); break;
            }
        }
    }
}
