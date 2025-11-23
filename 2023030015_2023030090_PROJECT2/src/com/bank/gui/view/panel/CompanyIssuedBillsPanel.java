package com.bank.gui.view.panel;

import com.bank.manager.BillManager;
import com.bank.model.accounts.Account;
import com.bank.model.bills.Bill;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompanyIssuedBillsPanel extends JPanel {

    public CompanyIssuedBillsPanel(User user, BillManager billManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Issued Bills", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        StringBuilder sb = new StringBuilder();
        List<Bill> allBills = billManager.getAllBills();
        boolean found = false;

        for (Bill bill : allBills) {
            Account issuer = bill.getIssuer();
            if (issuer != null && issuer.getOwner().equals(user)) {
                found = true;
                sb.append("Bill Number: ").append(bill.getBillNumber()).append("\n")
                  .append("RF Code: ").append(bill.getPaymentCode()).append("\n")
                  .append("Amount: ").append(String.format("%.2f €", bill.getAmount())).append("\n")
                  .append("Due Date: ").append(bill.getDueDate()).append("\n")
                  .append("Paid: ").append(bill.isPaid() ? "Yes" : "No").append("\n")
                  .append("------------------------------\n");
            }
        }

        if (!found) {
            sb.append("No issued bills found for this company.");
        }

       
        resultArea.setText(sb.toString());
    }


}