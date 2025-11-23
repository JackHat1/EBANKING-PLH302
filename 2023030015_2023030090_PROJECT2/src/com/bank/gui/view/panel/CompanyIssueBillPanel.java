package com.bank.gui.view.panel;

import com.bank.manager.*;
import com.bank.model.accounts.Account;
import com.bank.model.bills.Bill;
import com.bank.model.users.Customer;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompanyIssueBillPanel extends JPanel {

    public CompanyIssueBillPanel(User companyUser, AccountManager accountManager, UserManager userManager, BillManager billManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Issue New Bill", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField customerVatField = new JTextField(12);
        JTextField amountField = new JTextField(12);
        JTextField rfCodeField = new JTextField(12);
        JTextField billNumberField = new JTextField(12);

        JButton issueButton = new JButton("Issue");
        issueButton.setBackground(new Color(0, 153, 76));
        issueButton.setForeground(Color.WHITE);
        issueButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Customer VAT:"), gbc);
        gbc.gridx = 1;
        form.add(customerVatField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Amount (€):"), gbc);
        gbc.gridx = 1;
        form.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("RF Code:"), gbc);
        gbc.gridx = 1;
        form.add(rfCodeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Bill Number:"), gbc);
        gbc.gridx = 1;
        form.add(billNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(issueButton, gbc);

        add(form, BorderLayout.CENTER);

        issueButton.addActionListener(e -> {
            String vat = customerVatField.getText().trim();
            String rf = rfCodeField.getText().trim();
            String billNumber = billNumberField.getText().trim();

            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (vat.isEmpty() || rf.isEmpty() || billNumber.isEmpty() || amount <= 0) {
                    throw new IllegalArgumentException();
                }

                Customer customer = userManager.findByVat(vat);
                if (customer == null) {
                    JOptionPane.showMessageDialog(this, "Customer with VAT " + vat + " not found.");
                    return;
                }

                List<Account> accounts = accountManager.getAllAccounts();
                Account companyAccount = accounts.stream()
                        .filter(acc -> acc.getOwner().equals(companyUser))
                        .findFirst()
                        .orElse(null);

                if (companyAccount == null) {
                    JOptionPane.showMessageDialog(this, "No company account found.");
                    return;
                }

                Bill bill = new Bill(billNumber, rf, amount, companyAccount);
                billManager.createBill(bill);
                billManager.saveAll();

                JOptionPane.showMessageDialog(this, "Bill issued successfully.");
                customerVatField.setText("");
                amountField.setText("");
                rfCodeField.setText("");
                billNumberField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
   
   
    }



    
}