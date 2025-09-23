package com.tuc.ebanking.app;

public class App {
    public static void main(String[] args) {
        boolean useCli = false;
        for (String a : args) {
            if ("--cli".equalsIgnoreCase(a)) useCli = true;
            if ("--gui".equalsIgnoreCase(a)) useCli = false;
        }
        if (useCli) {
            com.tuc.ebanking.ui.cli.Cli.start();
        } else {
            com.tuc.ebanking.ui.gui.Gui.start();
        }
    }
}
