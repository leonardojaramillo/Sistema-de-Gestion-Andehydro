package main.java.com.andehydro.main;

import main.java.com.andehydro.ui.LoginScreen;

public class Main {
    public static void main(String[] args) {
        // Asegurar que la UI se cargue en el hilo correcto
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginScreen login = new LoginScreen();
            login.setVisible(true);
        });
    }
}
