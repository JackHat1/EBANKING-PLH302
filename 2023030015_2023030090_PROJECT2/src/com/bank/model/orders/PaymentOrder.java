package com.bank.model.orders;

import java.time.LocalDate;
import com.bank.model.bills.Bill;
import com.bank.model.transactions.Payment;
import com.bank.model.transactions.Transaction;
import com.bank.model.users.User;
import com.bank.manager.AccountManager;
import com.bank.manager.BillManager;
import com.bank.manager.TransactionManager;
import com.bank.model.accounts.Account;


public class PaymentOrder extends StandingOrder{

    private double maxAmount= 1000;
    private int failedAttempts= 0;
    private String fromIban;
    private String paymentCode;
    private String customerVat;
    private double fee;
    

    public PaymentOrder(String title, String description, String paymentCode, String fromIban, double maxAmount, LocalDate startingDate, LocalDate endingDate){
        super(title , description, startingDate, endingDate);
        this.paymentCode= paymentCode;
        this.fromIban=  fromIban;
        this.maxAmount = maxAmount;
    }


    @Override
    public void execute(LocalDate paymentDay, BillManager billMan, AccountManager accountMan, TransactionManager transMan, User user){  

        Bill bill = billMan.getBillByRF(paymentCode);
        Account from= accountMan.findByIban(fromIban);


        if(bill.getDueDate().isEqual(paymentDay)){

            if(bill.getAmount() <= maxAmount ){

                if(bill.getAmount() <= from.getBalance()){
                    Transaction transaction= new Payment(bill, from, bill.getIssuer(), user);
                    transMan.execute(transaction);

                    bill.setPaid(true);
                    failedAttempts= 0;
                    System.out.println("Payment executed for bill " + bill.getPaymentCode());
                } else{
                    failedAttempts++;
                    System.out.println("Payment failed due to insufficient funds.");
                }
            } else{
                failedAttempts++;
                System.out.println("Payment failed due to exceeding max amount.");
            }

        }

        if(failedAttempts > 3){
            System.out.println("Maximum failed attempts reached. ");
            this.setIsActive(false);
            

        }

    }

    @Override
    public boolean isFailed(){
        return failedAttempts>3;
    }

    
    @Override
    public String marshal() {
        StringBuffer sb = new StringBuffer();
        sb.append("type:PaymentOrder").append(",");
        sb.append("orderId:").append(getOrderId()).append(",");
        sb.append("paymentCode:").append(paymentCode).append(",");
        sb.append("title:").append(getTitle()).append(",");
        sb.append("description:").append(getDescription()).append(",");
        sb.append("customer:").append(customerVat).append(",");
        sb.append("maxAmount:").append(maxAmount).append(",");
        sb.append("startDate:").append(this.startingDate.format(formatter)).append(",");
        sb.append("endDate:").append(this.endingDate.format(formatter)).append(",");
        sb.append("fee:").append(fee).append(",");
        sb.append("chargeAccount:").append(fromIban);
        return sb.toString();
    }

    @Override
    public void unmarshal(String data) {
        String[] parts = data.split(",");

        for(String part: parts){
            String[] keyValue= part.split(":");
            String key = keyValue[0];
            String value= keyValue.length> 1 ? keyValue[1]: "";


            if(key.equals("orderId")){
                this.orderId = value;
            } else if(key.equals("paymentCode")){
                this.paymentCode= value;
            } else if(key.equals("title")){
                this.title= value;
            } else if(key.equals("description")){
                this.description= value;
            } else if(key.equals("customer")){
                this.customerVat= value;
            } else if(key.equals("maxAmount")){
                this.maxAmount= Double.parseDouble(value);
            } else if(key.equals("startDate")){
                this.startingDate= LocalDate.parse(value);
            } else if(key.equals("endDate")){
                this.endingDate= LocalDate.parse(value);
            } else if(key.equals("fee")){
                this.fee= Double.parseDouble(value);
            } else if(key.equals("chargeAccount")){
                this.fromIban= value;
            }

        }
        
    }

    
    
}
