package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.model.accounts.Account;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminAccountListPanel extends JPanel {

    public AdminAccountListPanel(AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("User Accounts", SwingConstants.CENTER);
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
        List<Account> accounts = accountManager.getAllAccounts();

        if (accounts.isEmpty()) {
            sb.append("No registered accounts found.");
        } else {
            for (Account acc : accounts) {
                sb.append("IBAN: ").append(acc.getIban()).append("\n")
                  .append("Owner: ").append(acc.getOwner().getFullName()).append("\n")
                  .append("Balance: ").append(String.format("%.2f €", acc.getBalance())).append("\n")
                  .append("------------------------------\n");
            }
        }

        resultArea.setText(sb.toString());
    }
}