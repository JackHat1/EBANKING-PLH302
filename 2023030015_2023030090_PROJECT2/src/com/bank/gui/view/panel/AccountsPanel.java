package com.bank.gui.view.panel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.bank.manager.AccountManager;
import com.bank.model.accounts.Account;
import com.bank.model.users.User;

public class AccountsPanel extends JPanel {

    public AccountsPanel(User user, AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("My Accounts", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        List<Account> accounts = accountManager.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            
            if (acc.getOwner().equals(user)) {
                sb.append("IBAN: ").append(acc.getIban())
                  .append("\nBalance: ").append(String.format("%.2f €", acc.getBalance()))
                  .append("\n\n");
            }
        }

        area.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
    }
}