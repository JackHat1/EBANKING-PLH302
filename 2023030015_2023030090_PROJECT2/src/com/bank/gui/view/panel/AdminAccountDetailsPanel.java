package com.bank.gui.view.panel;

import com.bank.manager.AccountManager;
import com.bank.model.accounts.*;
import com.bank.model.users.Individual;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminAccountDetailsPanel extends JPanel {

    public AdminAccountDetailsPanel(AccountManager accountManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Account Details", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(Color.WHITE);

        JLabel ibanLabel = new JLabel("Enter IBAN:");
        JTextField ibanField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(0, 102, 204));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        inputPanel.add(ibanLabel);
        inputPanel.add(ibanField);
        inputPanel.add(searchBtn);
        add(inputPanel, BorderLayout.CENTER);

        JTextArea resultArea = new JTextArea(12, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> {
            String iban = ibanField.getText().trim();
            Account acc = accountManager.findByIban(iban);
            if (acc == null) {
                resultArea.setText("Account not found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Account Info:\n");
            sb.append("IBAN          : ").append(acc.getIban()).append("\n");
            sb.append("Owner         : ").append(acc.getOwner().getFullName()).append(" (").append(acc.getOwner().getVat()).append(")").append("\n");
            sb.append("Balance       : ").append(String.format("%.2f €", acc.getBalance())).append("\n");
            sb.append("Interest Rate : ").append(String.format("%.2f %%", acc.getInterestRate() * 100)).append("\n");
            sb.append("Created On    : ").append(acc.getDateCreated()).append("\n");

            if (acc instanceof BusinessAccount) {
                sb.append("Type         : Business Account\n");
                sb.append("Monthly Fee  : ").append(((BusinessAccount) acc).getMonthlyFee()).append(" €\n");
            } else if (acc instanceof PersonalAccount) {
                sb.append("Type         : Personal Account\n");
                List<Individual> coOwners = ((PersonalAccount) acc).getCoOwners();
                if (coOwners.isEmpty()) {
                    sb.append("Co-Owners    : None\n");
                } else {
                    sb.append("Co-Owners    :\n");
                    for (Individual co : coOwners) {
                        sb.append("   - ").append(co.getFullName()).append(" (").append(co.getVat()).append(")\n");
                    }
                }
            } else {
                sb.append("Type         : Unknown\n");
            }

            resultArea.setText(sb.toString());
        });
    }
}