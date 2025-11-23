package com.bank.gui.view.panel;

import com.bank.manager.*;
import com.bank.model.accounts.Account;
import com.bank.model.bills.Bill;
import com.bank.model.transactions.Payment;
import com.bank.model.users.Customer;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPayBillPanel extends JPanel {

    private JTextField vatField;
    private JComboBox<String> accountBox;
    private JTextField rfField;

    public AdminPayBillPanel(AccountManager accountManager, UserManager userManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Pay Customer's Bill", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        vatField = new JTextField(15);
        rfField = new JTextField(15);
        accountBox = new JComboBox<>();

        JButton loadAccountsBtn = new JButton("Load Accounts");
        JButton payBtn = new JButton("Pay");

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Customer VAT:"), gbc);
        gbc.gridx = 1;
        form.add(vatField, gbc);

        gbc.gridx = 2;
        form.add(loadAccountsBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Select Account:"), gbc);
        gbc.gridx = 1;
        form.add(accountBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("RF Code:"), gbc);
        gbc.gridx = 1;
        form.add(rfField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        form.add(payBtn, gbc);

        add(form, BorderLayout.CENTER);

        loadAccountsBtn.addActionListener(e -> {
            String vat = vatField.getText().trim();
            Customer customer = userManager.findByVat(vat);
            accountBox.removeAllItems();

            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found.");
                return;
            }

            List<Account> accounts = accountManager.getAllAccounts().stream()
                .filter(acc -> acc.getOwner().equals(customer))
                .collect(Collectors.toList());

            if (accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer has no accounts.");
                return;
            }

            for (Account acc : accounts) {
                accountBox.addItem(acc.getIban());
            }
        });

        payBtn.addActionListener(e -> {
            String vat = vatField.getText().trim();
            String rf = rfField.getText().trim();
            String iban = (String) accountBox.getSelectedItem();

            if (vat.isEmpty() || rf.isEmpty() || iban == null) {
                JOptionPane.showMessageDialog(this, "Please complete all fields.");
                return;
            }

            Customer customer = userManager.findByVat(vat);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found.");
                return;
            }

            Account from = accountManager.findByIban(iban);
            if (from == null) {
                JOptionPane.showMessageDialog(this, "Invalid IBAN.");
                return;
            }

            BillManager billManager = new BillManager(accountManager, userManager);
            billManager.loadBills();
            Bill bill = billManager.getBillByRF(rf);

            if (bill == null) {
                JOptionPane.showMessageDialog(this, "Bill not found.");
                return;
            }

            if (bill.isPaid()) {
                JOptionPane.showMessageDialog(this, "Bill already paid.");
                return;
            }

            if (bill.getAmount() > from.getBalance()) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.");
                return;
            }

            TransactionManager tm = new TransactionManager();
            tm.execute(new Payment(bill, from, bill.getIssuer(), customer));
            bill.setPaid(true);
            billManager.saveAll();

            JOptionPane.showMessageDialog(this, "Bill paid successfully.");
        });
    }
}