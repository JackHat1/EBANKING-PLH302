package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.manager.UserManager;
import com.bank.model.accounts.*;
import com.bank.model.users.*;

import javax.swing.*;
import java.awt.*;

public class AdminCreateAccountPanel extends JPanel {

    public AdminCreateAccountPanel(AccountManager accountManager, UserManager userManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] types = {"PersonalAccount", "BusinessAccount"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JTextField vatField = new JTextField(15);
        JTextField rateField = new JTextField(15);
        JTextField feeField = new JTextField(15); 
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(0, 102, 204));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);

        
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; form.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Customer VAT:"), gbc);
        gbc.gridx = 1; form.add(vatField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Interest Rate (%):"), gbc);
        gbc.gridx = 1; form.add(rateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Monthly Fee (€):"), gbc);
        gbc.gridx = 1; form.add(feeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(createBtn, gbc);

        add(form, BorderLayout.CENTER);

      
        typeBox.addActionListener(e -> {
            boolean isBusiness = typeBox.getSelectedItem().equals("BusinessAccount");
            feeField.setEnabled(isBusiness);
        });
        feeField.setEnabled(false);

      
        createBtn.addActionListener(e -> {
            String type = (String) typeBox.getSelectedItem();
            String vat = vatField.getText().trim();
            String rateStr = rateField.getText().trim();
            String feeStr = feeField.getText().trim();

            
            if (vat.isEmpty() || rateStr.isEmpty() || (type.equals("BusinessAccount") && feeStr.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            Customer customer = userManager.findByVat(vat);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer with VAT " + vat + " not found.");
                return;
            }

            try {
                double rate = Double.parseDouble(rateStr);
                Account acc = null;

                if (type.equals("PersonalAccount")) {
                    if (!(customer instanceof Individual)) {
                        JOptionPane.showMessageDialog(this, "Only individuals are allowed for personal account.");
                        return;
                    }
                    acc = new PersonalAccount((Individual) customer, rate);

                } else if (type.equals("BusinessAccount")) {
                    if (!(customer instanceof Company)) {
                        JOptionPane.showMessageDialog(this, "Only companies are allowed for business account.");
                        return;
                    }
                    double fee = Double.parseDouble(feeStr);
                    acc = new BusinessAccount((Company) customer, rate, fee);
                }

                accountManager.addAccount(acc);
                accountManager.saveAll();
                JOptionPane.showMessageDialog(this, "Account created with IBAN:\n" + acc.getIban());

                vatField.setText("");
                rateField.setText("");
                feeField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid interest rate or fee.");
            }
        });
    }
}