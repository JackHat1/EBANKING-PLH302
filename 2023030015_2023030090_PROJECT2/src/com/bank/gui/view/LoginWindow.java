package com.bank.gui.view;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("Login");
    private LoginListener loginListener;

    public LoginWindow() {
        setTitle("Bank Of TUC - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 51, 102));
        header.setPreferredSize(new Dimension(400, 80));

        ImageIcon logoIcon = new ImageIcon(new ImageIcon("./data/logo/tuc.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel titleLabel = new JLabel("BANK OF TUC", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        header.add(logoLabel, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(Color.WHITE);
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            if (loginListener != null) {
                loginListener.onLogin(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });

        setVisible(true);
    }

    public interface LoginListener {
        void onLogin(String username, String password);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }



    
}