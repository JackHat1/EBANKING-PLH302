package com.tuc.ebanking.ui.gui;

import javax.swing.*;

public class Gui {
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("E-Banking — Bank of TUC (placeholder)");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setSize(640, 400);
            f.add(new JLabel("GUI placeholder — υλοποίηση αργότερα", SwingConstants.CENTER));
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
