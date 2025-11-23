package com.bank.gui.view.panel;

import com.bank.manager.StandingOrderManager;
import com.bank.model.orders.StandingOrder;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminStandingOrdersPanel extends JPanel {

    public AdminStandingOrdersPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Standing Orders", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Load Orders");
        refreshBtn.setBackground(new Color(0, 102, 204));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        add(refreshBtn, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> {
            StandingOrderManager manager = new StandingOrderManager();
            manager.loadOrders();
            List<StandingOrder> orders = manager.getAllOrders();

            if (orders.isEmpty()) {
                resultArea.setText("No standing orders found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (StandingOrder order : orders) {
                sb.append("ID: ").append(order.getOrderId()).append("\n");
                sb.append("   Title      : ").append(order.getTitle()).append("\n");
                sb.append("   Description: ").append(order.getDescription()).append("\n");
                sb.append("   Period     : ").append(order.getStartingDate()).append(" -> ").append(order.getEndingDate()).append("\n");
                sb.append("   Active     : ").append(order.getIsActive() ? "Yes" : "No").append("\n");
                sb.append("   Expired    : ").append(order.isExpired() ? "Yes" : "No").append("\n");
                sb.append("   Failed     : ").append(order.isFailed() ? "Yes" : "No").append("\n");
                sb.append("--------------------------------------------------\n");
            }

            resultArea.setText(sb.toString());
        });
  
    }



}