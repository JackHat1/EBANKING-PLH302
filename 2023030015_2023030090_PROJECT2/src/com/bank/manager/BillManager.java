package com.bank.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bank.model.accounts.Account;
import com.bank.model.accounts.BusinessAccount;
import com.bank.model.bills.Bill;
import com.bank.model.users.Company;
import com.bank.model.users.Customer;
import com.bank.storage.CsvStorageManager;

public class BillManager {

    private final List<Bill> issuedBills = new ArrayList<>();
    private final List<Bill> paidBills = new ArrayList<>();
    private final String billsFolder = "./data/bills/";
    private final String issuedPath = billsFolder + "issued.csv";
    private final String paidPath = billsFolder + "paid.csv";

    private final AccountManager accountManager;
    private final UserManager userManager;
    private final CsvStorageManager storage = new CsvStorageManager();

    public BillManager(AccountManager accountManager, UserManager userManager) {
        this.accountManager = accountManager;
        this.userManager = userManager;
        initializeStorageFiles();
        loadBills();
    }

    private void initializeStorageFiles() {
        File issuedFile = new File(issuedPath);
        File paidFile = new File(paidPath);
        try {
            if (!issuedFile.exists()) issuedFile.createNewFile();
            if (!paidFile.exists()) paidFile.createNewFile();
        } catch (Exception e) {
            System.err.println("Failed to initialize issued/paid CSV files: " + e.getMessage());
        }
    }

    public void createBill(Bill bill) {
        issuedBills.removeIf(b -> b.getPaymentCode().equals(bill.getPaymentCode()));
        issuedBills.add(bill);

        String date = bill.getIssueDate().toString();
        String filePath = billsFolder + date + ".csv";
        List<String> lines = storage.loadLines(filePath);
        lines.removeIf(l -> l.contains("paymentCode:" + bill.getPaymentCode()));
        lines.add(stripIsPaid(bill.marshal()));
        storage.saveLines(lines, filePath, false);

        saveAll();
    }

    public void payBill(Bill bill) {
        if (bill.isPaid()) {
            return;
        }

        bill.setPaid(true);
        issuedBills.removeIf(b -> b.getPaymentCode().equals(bill.getPaymentCode()));
        paidBills.removeIf(b -> b.getPaymentCode().equals(bill.getPaymentCode()));
        paidBills.add(bill);

        updateDailyFile(bill);
        saveAll();
    
            List<Bill> single = new ArrayList<>();
            single.add(bill);
            storage.saveAll(single, paidPath, true); 

            updateDailyFile(bill);     
            saveAll();                 
    }
    

    private void updateDailyFile(Bill bill) {
        String fileName = billsFolder + bill.getIssueDate().toString() + ".csv";
        List<String> lines = storage.loadLines(fileName);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (line.contains("paymentCode:" + bill.getPaymentCode())) {
                updatedLines.add(stripIsPaid(bill.marshal()));
            } else {
                updatedLines.add(line);
            }
        }

        storage.saveLines(updatedLines, fileName, false);
    }

    public void loadBills() {
        issuedBills.clear();
        paidBills.clear();

        List<String> issuedLines = storage.loadLines(issuedPath);

        for (String line : issuedLines) {

            Bill bill = parseBill(line);
            if (bill != null && !bill.isPaid()) issuedBills.add(bill);
        }

        List<String> paidLines = storage.loadLines(paidPath);

        for (String line : paidLines) {

            Bill bill = parseBill(line);

            if (bill != null) {
                bill.setPaid(true);
                paidBills.add(bill);
            }
        }

        File folder = new File(billsFolder);
        File[] files = folder.listFiles((dir, name) -> name.matches("\\d{4}-\\d{2}-\\d{2}\\.csv"));
        if (files != null) {

            for (File file : files) {

                List<String> lines = storage.loadLines(file.getPath());
                for (String line : lines) {
                    Bill bill = parseBill(line);
                    if (bill == null) continue;
                    if (!bill.isPaid()) {
                        if (issuedBills.stream().noneMatch(b -> b.getPaymentCode().equals(bill.getPaymentCode()))) {
                            issuedBills.add(bill);
                        }
                    }
                }
            }
        }


        /*List<String> issuedLines = storage.loadLines(issuedPath);
        for (String line : issuedLines) {
            Bill bill = parseBill(line);
            if (bill != null && !bill.isPaid() &&
                issuedBills.stream().noneMatch(b -> b.getPaymentCode().equals(bill.getPaymentCode()))) {
                issuedBills.add(bill);
            }
        }

        
        List<String> paidLines = storage.loadLines(paidPath);
        for (String line : paidLines) {
            Bill bill = parseBill(line);
            if (bill != null &&
                paidBills.stream().noneMatch(b -> b.getPaymentCode().equals(bill.getPaymentCode()))) {
                bill.setPaid(true);
                paidBills.add(bill);
            }
        }*/
    }


    private Bill parseBill(String line) {
        if (!line.contains("isPaid:")) line += ",isPaid:false";

        Bill bill = new Bill("", "", 0.0, null);
        bill.unmarshal(line);

        String issuerVat = bill.getIssuerVat();
        Customer company = userManager.findByVat(issuerVat);

        if (!(company instanceof Company)){
            return null;
        } 

        for (Account acc : accountManager.getAllAccounts()) {

            if (acc instanceof BusinessAccount && acc.getOwner().equals(company)) {
                bill.setIssuer(acc);
                return bill;
            }
        }

        return null;
    }

    private String stripIsPaid(String line) {
        return line.replaceAll(",isPaid:(true|false)", "");
    }

    public void saveAll() {
        List<String> issuedLines = new ArrayList<>();
        for (Bill b : issuedBills) issuedLines.add(stripIsPaid(b.marshal()));
        storage.saveLines(issuedLines, issuedPath, false);

        List<String> paidLines = new ArrayList<>();
        for (Bill b : paidBills) paidLines.add(b.marshal());
        storage.saveLines(paidLines, paidPath, false);
    }


    public List<Bill> getAllBills() {
        List<Bill> all = new ArrayList<>();
        all.addAll(issuedBills);
        all.addAll(paidBills);
        return all;
    }

    public List<Bill> getIssuedBills() {
        return new ArrayList<>(issuedBills);
    }

    public List<Bill> getPaidBills() {
        return new ArrayList<>(paidBills);
    }

    public Bill getBillByRF(String rfCode) {
        for (Bill bill : getAllBills()) {
            if (bill.getPaymentCode().equals(rfCode)) {
                return bill;
            }
        }
        return null;
    }
}
