package com.bank.gui.view.panel;

import com.bank.manager.UserManager;
import com.bank.model.users.Customer;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminCustomerListPanel extends JPanel {

    public AdminCustomerListPanel(UserManager userManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Customer List", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        List<User> allUsers = userManager.getAllUsers();
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        

        for (User user : allUsers) {
            if (user instanceof Customer customer) {
                found = true;
                sb.append("Full Name: ").append(customer.getFullName()).append("\n")
                  .append("Username: ").append(customer.getUsername()).append("\n")
                  .append("VAT: ").append(customer.getVat()).append("\n")
                  .append("Role: ").append(customer.getRole()).append("\n")
                  .append("------------------------------\n");
            }
        }

        if (!found) {
            sb.append("No registered customers found.");
        }

        area.setText(sb.toString());
    }
}