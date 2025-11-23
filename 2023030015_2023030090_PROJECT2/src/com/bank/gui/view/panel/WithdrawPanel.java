package com.bank.gui.view.panel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.bank.model.accounts.*;
import com.bank.model.users.User;
import com.bank.manager.AccountManager;
import com.bank.manager.TransactionManager;
import com.bank.model.transactions.Withdrawal;

public class WithdrawPanel extends JPanel {
    public WithdrawPanel(User user, AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Withdraw Money", JLabel.CENTER);
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

        JComboBox<String> accountBox = new JComboBox<>();
        for (Account acc : myAccounts) accountBox.addItem(acc.getIban());

        JTextField amountField = new JTextField(12);
        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBackground(new Color(150, 30, 30));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFocusPainted(false);

        JLabel balanceLabel = new JLabel("Balance: - €");
        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        balanceLabel.setForeground(Color.DARK_GRAY);

        accountBox.addActionListener(e -> {
            String iban = (String) accountBox.getSelectedItem();
            Account selected = accountManager.findByIban(iban);
            if (selected != null) {
                balanceLabel.setText("Balance: " + String.format("%.2f €", selected.getBalance()));
            }
        });

        if (!myAccounts.isEmpty()) {
            Account selected = accountManager.findByIban((String) accountBox.getSelectedItem());
            balanceLabel.setText("Balance: " + String.format("%.2f €", selected.getBalance()));
        }

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Account:"), gbc);
        gbc.gridx = 1;
        form.add(accountBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Amount (€):"), gbc);
        gbc.gridx = 1;
        form.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        form.add(balanceLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(withdrawBtn, gbc);

        add(form, BorderLayout.CENTER);

        withdrawBtn.addActionListener(e -> {
            String iban = (String) accountBox.getSelectedItem();
            Account selected = accountManager.findByIban(iban);


            try {
                double amount = Double.parseDouble(amountField.getText());

                if (amount <= 0) throw new NumberFormatException();

                if (selected.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance.");
                    return;
                }

                TransactionManager tm = new TransactionManager();
                Withdrawal tx = new Withdrawal(selected, amount, user, "Withdraw via GUI");
                tm.execute(tx);

                JOptionPane.showMessageDialog(this, "Withdrawn " + amount + "€ from " + selected.getIban());
                amountField.setText("");
                balanceLabel.setText("Balance: " + String.format("%.2f €", selected.getBalance()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
          
          
            }
        });
  
  
  
    }


}