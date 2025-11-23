package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.manager.StatementManager;
import com.bank.model.accounts.Account;
import com.bank.model.statements.StatementEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminAccountStatementsPanel extends JPanel {

    private JComboBox<String> accountBox;
    private JTextArea resultArea;

    public AdminAccountStatementsPanel(AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Account Statements", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

 
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout());

        List<Account> allAccounts = accountManager.getAllAccounts();

        if (allAccounts.isEmpty()) {
            topPanel.add(new JLabel("No accounts in the system."));
            add(topPanel, BorderLayout.NORTH);
            return;
        }

        accountBox = new JComboBox<>();
        for (Account acc : allAccounts) {
            accountBox.addItem(acc.getIban());
        }

        JButton showBtn = new JButton("Show");
        topPanel.add(new JLabel("Account: "));
        topPanel.add(accountBox);
        topPanel.add(showBtn);
        add(topPanel, BorderLayout.NORTH);


        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        showBtn.addActionListener(e -> {
            String selectedIban = (String) accountBox.getSelectedItem();
            Account account = accountManager.findByIban(selectedIban);

            if (account == null) {
                resultArea.setText("Account not found.");
                return;
            }

            List<StatementEntry> entries = new StatementManager().load(account);

            if (entries.isEmpty()) {
                resultArea.setText("No statements for this account.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (StatementEntry entry : entries) {
                    sb.append(entry).append("\n");
                }
                resultArea.setText(sb.toString());
            }
        });
    }
}