package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.manager.TransactionManager;
import com.bank.model.accounts.Account;
import com.bank.model.transactions.Transfer;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransferPanel extends JPanel {

    public TransferPanel(User user, AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Transfer Money", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Account> myAccounts = accountManager.getAllAccounts().stream()
            .filter(acc -> acc.getOwner().equals(user))
            .toList();

        JComboBox<String> fromBox = new JComboBox<>();
        for (Account acc : myAccounts) fromBox.addItem(acc.getIban());

        JTextField toIbanField = new JTextField(12);
        JTextField amountField = new JTextField(12);
        JTextField reasonField = new JTextField(12);
        JLabel balanceLabel = new JLabel("Balance: - €");
        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        balanceLabel.setForeground(Color.DARK_GRAY);

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBackground(new Color(0, 102, 204));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFocusPainted(false);

        fromBox.addActionListener(e -> {
            String iban = (String) fromBox.getSelectedItem();
            Account selected = accountManager.findByIban(iban);
            if (selected != null) {
                balanceLabel.setText("Balance: " + String.format("%.2f €", selected.getBalance()));
            }
        });
        if (!myAccounts.isEmpty()) {
            Account selected = accountManager.findByIban((String) fromBox.getSelectedItem());
            balanceLabel.setText("Balance: " + String.format("%.2f €", selected.getBalance()));
        }

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("From Account:"), gbc);
        gbc.gridx = 1;
        form.add(fromBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("To IBAN:"), gbc);
        gbc.gridx = 1;
        form.add(toIbanField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Amount (€):"), gbc);
        gbc.gridx = 1;
        form.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        form.add(reasonField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        form.add(balanceLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        form.add(transferBtn, gbc);

        add(form, BorderLayout.CENTER);

        transferBtn.addActionListener(e -> {
            String fromIban = (String) fromBox.getSelectedItem();
            Account from = accountManager.findByIban(fromIban);
            Account to = accountManager.findByIban(toIbanField.getText().trim());

            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                String reason = reasonField.getText().trim();

                if (amount <= 0 || reason.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                    return;
                }

                if (to == null || from.getIban().equals(to.getIban())) {
                    JOptionPane.showMessageDialog(this, "Invalid or same recipient IBAN.");
                    return;
                }

                if (from.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance.");
                    return;
                }

                TransactionManager tm = new TransactionManager();
                Transfer tx = new Transfer(from, to, amount, user, reason, reason);
                tm.execute(tx);

                JOptionPane.showMessageDialog(this, "Transferred " + amount + "€ to " + to.getIban());
                amountField.setText("");
                toIbanField.setText("");
                reasonField.setText("");
                balanceLabel.setText("Balance: " + String.format("%.2f €", from.getBalance()));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
     
     
     
        });
    }


    
}