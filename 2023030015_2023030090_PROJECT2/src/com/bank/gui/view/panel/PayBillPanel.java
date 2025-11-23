package com.bank.gui.view.panel;

import com.bank.manager.*;
import com.bank.model.accounts.Account;
import com.bank.model.bills.Bill;
import com.bank.model.transactions.Payment;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PayBillPanel extends JPanel {
    public PayBillPanel(User user, AccountManager accountManager, BillManager billManager, TransactionManager transactionManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Pay Bill (RF Code)", JLabel.CENTER);
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

        JTextField rfField = new JTextField(12);
        JLabel balanceLabel = new JLabel("Balance: - €");
        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        balanceLabel.setForeground(Color.DARK_GRAY);

        JButton payBtn = new JButton("Pay");
        payBtn.setBackground(new Color(0, 153, 76));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);

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
        form.add(new JLabel("From Account:"), gbc);
        gbc.gridx = 1;
        form.add(accountBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("RF Code:"), gbc);
        gbc.gridx = 1;
        form.add(rfField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        form.add(balanceLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(payBtn, gbc);

        add(form, BorderLayout.CENTER);

        payBtn.addActionListener(e -> {
            String fromIban = (String) accountBox.getSelectedItem();
            Account from = accountManager.findByIban(fromIban);
            String rf = rfField.getText().trim();

            if (rf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the RF code.");
                return;
            }

            Bill bill = billManager.getBillByRF(rf);
            if (bill == null) {
                JOptionPane.showMessageDialog(this, "No bill found with the given RF code.");
                return;
            }

            if (bill.isPaid()) {
                JOptionPane.showMessageDialog(this, "This bill is already paid.");
                return;
            }

            if (from.getBalance() < bill.getAmount()) {
                JOptionPane.showMessageDialog(this, "Insufficient balance for payment.");
                return;
            }

            Account business = bill.getIssuer();
            transactionManager.execute(new Payment(bill, from, business, user));

            JOptionPane.showMessageDialog(this, "Payment of " + bill.getAmount() + "€ for bill " + rf + " completed.");
            rfField.setText("");
            balanceLabel.setText("Balance: " + String.format("%.2f €", from.getBalance()));
        });
    }






    
}