package main.java.com.andehydro.ui;

import main.java.com.andehydro.util.ConnectionUtil;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginScreen extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JTextField txtHost;
    private JTextField txtPort;
    private JTextField txtService;
    private JButton btnConectar;

    public LoginScreen() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Login - Conexión Oracle");
        setSize(360, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(30, 30, 100, 20);

        txtUser = new JTextField();
        txtUser.setBounds(140, 30, 160, 25);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(30, 70, 100, 20);

        txtPass = new JPasswordField();
        txtPass.setBounds(140, 70, 160, 25);

        JLabel lblHost = new JLabel("Host:");
        lblHost.setBounds(30, 110, 100, 20);

        txtHost = new JTextField("localhost");
        txtHost.setBounds(140, 110, 160, 25);

        JLabel lblPort = new JLabel("Puerto:");
        lblPort.setBounds(30, 150, 100, 20);

        txtPort = new JTextField("1521");
        txtPort.setBounds(140, 150, 160, 25);

        JLabel lblService = new JLabel("Servicio:");
        lblService.setBounds(30, 190, 100, 20);

        txtService = new JTextField("xe");
        txtService.setBounds(140, 190, 160, 25);

        btnConectar = new JButton("Conectar");
        btnConectar.setBounds(110, 240, 120, 35);
        btnConectar.addActionListener(e -> conectar());

        add(lblUser);
        add(txtUser);
        add(lblPass);
        add(txtPass);
        add(lblHost);
        add(txtHost);
        add(lblPort);
        add(txtPort);
        add(lblService);
        add(txtService);
        add(btnConectar);
    }

    private void conectar() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String host = txtHost.getText();
        String port = txtPort.getText();
        String service = txtService.getText();

        ConnectionUtil.setCredentials(user, pass, host, port, service);

        try {
            Connection conn = ConnectionUtil.getConnection();
            conn.close();

            JOptionPane.showMessageDialog(this, "Conexión exitosa.");

            new MainMenuFrame().setVisible(true);
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage());
        }
    }
}
