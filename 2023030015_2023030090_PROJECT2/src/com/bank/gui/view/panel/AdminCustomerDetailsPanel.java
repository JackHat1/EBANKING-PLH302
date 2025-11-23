package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.manager.UserManager;
import com.bank.model.accounts.Account;
import com.bank.model.users.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminCustomerDetailsPanel extends JPanel {

    public AdminCustomerDetailsPanel(UserManager userManager, AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Customer Details", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(Color.WHITE);

        JLabel vatLabel = new JLabel("Enter VAT:");
        JTextField vatField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(0, 102, 204));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        inputPanel.add(vatLabel);
        inputPanel.add(vatField);
        inputPanel.add(searchBtn);
        add(inputPanel, BorderLayout.CENTER);

        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> {
            String vat = vatField.getText().trim();
            Customer customer = userManager.findByVat(vat);
            if (customer == null) {
                resultArea.setText("Customer not found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Customer Details:\n");
            sb.append("Full Name: ").append(customer.getFullName()).append("\n");
            sb.append("Username : ").append(customer.getUsername()).append("\n");
            sb.append("VAT      : ").append(customer.getVat()).append("\n");
            sb.append("Role     : ").append(customer.getRole()).append("\n\n");

            sb.append("Accounts:\n");
            List<Account> accounts = accountManager.getAllAccounts();
            boolean hasAccounts = false;
            
            for (Account acc : accounts) {
                if (acc.getOwner().equals(customer)) {
                    sb.append(" - IBAN: ").append(acc.getIban())
                      .append(" | Balance: ").append(String.format("%.2f €", acc.getBalance()))
                      .append("\n");
                    hasAccounts = true;
                }
            }

            if (!hasAccounts) sb.append("No accounts found for this customer.");

            resultArea.setText(sb.toString());
        });
    }
}