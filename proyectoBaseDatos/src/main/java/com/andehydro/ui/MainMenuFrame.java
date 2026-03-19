package main.java.com.andehydro.ui;

import javax.swing.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema Administrador - Menú Principal");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("Sistema Gestor BDA ANDEHYDRO");
        lblTitle.setBounds(120, 20, 200, 25);

        JButton btnClientes = new JButton("Gestionar Clientes");
        btnClientes.setBounds(110, 60, 200, 30);
        btnClientes.addActionListener(e -> {
            ClienteManagementFrame f = new ClienteManagementFrame();
            f.setVisible(true);
        });

        JButton btnCentrales = new JButton("Gestionar Centrales");
        btnCentrales.setBounds(110, 100, 200, 30);
        btnCentrales.addActionListener(e -> {
            CentralManagementFrame f = new CentralManagementFrame();
            f.setVisible(true);
        });

        JButton btnProyectos = new JButton("Gestionar Proyectos");
        btnProyectos.setBounds(110, 140, 200, 30);
        btnProyectos.addActionListener(e -> {
            ProyectoManagementFrame f = new ProyectoManagementFrame();
            f.setVisible(true);
        });

        JButton btnFacturas = new JButton("Gestionar Facturas");
        btnFacturas.setBounds(110, 180, 200, 30);
        btnFacturas.addActionListener(e -> {
            FacturaManagementFrame f = new FacturaManagementFrame();
            f.setVisible(true);
        });

        JButton btnCompras = new JButton("Gestionar Compras");
        btnCompras.setBounds(110, 220, 200, 30);
        btnCompras.addActionListener(e -> {
            CompraManagementFrame f = new CompraManagementFrame();
            f.setVisible(true);
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(110, 260, 200, 30);
        btnSalir.addActionListener(e -> System.exit(0));

        add(lblTitle);
        add(btnClientes);
        add(btnCentrales);
        add(btnProyectos);
        add(btnFacturas);
        add(btnCompras);
        add(btnSalir);
    }
}
