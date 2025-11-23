package com.bank.gui.app;

import com.bank.manager.*;
import com.bank.model.users.*;
import com.bank.gui.view.*;

import javax.swing.*;

public class EBankingApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            AccountManager accountManager = new AccountManager(userManager);
            BillManager billManager = new BillManager(accountManager, userManager);

            try {
                userManager.load();
                accountManager.load();
                billManager.loadBills();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error loading data files.\nCheck the /data/ folders.",
                        "Failure", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            showLoginWindow(userManager, accountManager);
        });
    }

    private static void showLoginWindow(UserManager userManager, AccountManager accountManager) {
        LoginWindow login = new LoginWindow();
        login.setLoginListener((username, password) -> {
            User user = userManager.findByUsername(username);

            if (user != null && user.checkPassword(password)) {
                login.dispose();
                new MainDashboard(user, accountManager, userManager) {
                    @Override
                    public void dispose() {
                        
                        super.dispose();
                        EBankingApp.showLoginWindow(userManager, accountManager);
                    }
                };
            } else {
                JOptionPane.showMessageDialog(null, "Wrong login credentials.", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}