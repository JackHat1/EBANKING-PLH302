package com.bank.gui.view.panel;

import com.bank.manager.*;
import com.bank.model.accounts.Account;
import com.bank.model.accounts.BusinessAccount;
import com.bank.model.orders.StandingOrder;
import com.bank.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;


public class AdminSimulateTimePanel extends JPanel {

    private final JTextField dateField = new JTextField(15);
    private final JTextArea logArea = new JTextArea();

    public AdminSimulateTimePanel(AccountManager accountManager, UserManager userManager) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Simulate Time Passing", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(new JLabel("Target Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        JButton simulateBtn = new JButton("Simulate");
        simulateBtn.setBackground(new Color(0, 102, 204));
        simulateBtn.setForeground(Color.WHITE);
        simulateBtn.setFocusPainted(false);
        inputPanel.add(simulateBtn);
        add(inputPanel, BorderLayout.NORTH);

        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        simulateBtn.addActionListener(e -> simulate(accountManager, userManager));
    }

    private void simulate(AccountManager accountManager, UserManager userManager) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate;

        try {
            targetDate = LocalDate.parse(dateField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        if (targetDate.isBefore(today)) {
            JOptionPane.showMessageDialog(this, "Date must be in the future.");
            return;
        }

        logArea.setText("Simulating from " + today + " to " + targetDate + "...\n\n");

        BillManager billManager = new BillManager(accountManager, userManager);
        billManager.loadBills();
        TransactionManager transactionManager = new TransactionManager();

        StandingOrderManager orderManager = new StandingOrderManager();
        orderManager.loadOrders();

        while (!today.isAfter(targetDate)) {
            logArea.append(today + "\n");

           
            for (StandingOrder order : orderManager.getAllOrders()) {
                User user = userManager.findByVat(order.getDescription()); 
                if (user != null) {
                    order.execute(today, billManager, accountManager, transactionManager, user);
                }
            }

            
            for (Account acc : accountManager.getAllAccounts()) {
                if (today.getDayOfMonth() == 1) {
                    double interest = acc.getBalance() * acc.getInterestRate();
                    acc.deposit(interest);
                    logArea.append("  + Interest for " + acc.getIban() + ": " + String.format("%.2f", interest) + "\n");

                    if (acc instanceof BusinessAccount) {
                        double fee = ((BusinessAccount) acc).getMonthlyFee();
                        acc.withdraw(fee);
                        logArea.append("  - Fee for " + acc.getIban() + ": " + String.format("%.2f", fee) + "\n");
                    }
                }
            }

            today = today.plusDays(1);
        }

        orderManager.saveOrders(orderManager.getAllOrders()); 

        logArea.append("\nSimulation complete.\n");
    }
}