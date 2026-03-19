package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Cliente;
import main.java.com.andehydro.service.ClienteService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ClienteManagementFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;
    private ClienteService clienteService;

    public ClienteManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            clienteService = new ClienteService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            // no continuar si no hay conexión
            dispose();
            return;
        }
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Clientes");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[] {
                "ID", "Nombre", "Razón Social", "Tipo", "RUC", "Dirección", "Teléfono", "ID Central"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        // Buttons
        JPanel pnlButtons = new JPanel();
        JButton btnCrear = new JButton("Crear");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnVolver = new JButton("Volver");

        btnCrear.addActionListener(e -> onCrear());
        btnEditar.addActionListener(e -> onEditar());
        btnEliminar.addActionListener(e -> onEliminar());
        btnActualizar.addActionListener(e -> loadTableData());
        btnVolver.addActionListener(e -> {
            dispose();
        });

        pnlButtons.add(btnCrear);
        pnlButtons.add(btnEditar);
        pnlButtons.add(btnEliminar);
        pnlButtons.add(btnActualizar);
        pnlButtons.add(btnVolver);

        add(sp, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            List<Cliente> lista = clienteService.listarTodos();
            for (Cliente c : lista) {
                tableModel.addRow(new Object[] {
                        c.getIdCliente(),
                        c.getNombre(),
                        c.getRazonSocial(),
                        c.getTipoCliente(),
                        c.getRuc(),
                        c.getDireccion(),
                        c.getTelefono(),
                        c.getIdCentral()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar clientes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        ClienteFormDialog dialog = new ClienteFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Cliente nuevo = dialog.getCliente();
            try {
                clienteService.crearCliente(nuevo);
                JOptionPane.showMessageDialog(this, "Cliente creado correctamente.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando cliente: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Cliente c = clienteService.obtenerPorId(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
                return;
            }
            ClienteFormDialog dialog = new ClienteFormDialog(this, c);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                Cliente mod = dialog.getCliente();
                clienteService.actualizarCliente(mod);
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar cliente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            clienteService.eliminarCliente(id);
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // cerrar conexión cuando se cierra la ventana
    @Override
    public void dispose() {
        super.dispose();
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (Exception ignored) {}
    }

    // ---------- Dialogo simple para crear/editar clientes ----------
    private static class ClienteFormDialog extends JDialog {

        private final JTextField txtId = new JTextField(12);
        private final JTextField txtNombre = new JTextField(30);
        private final JTextField txtRazon = new JTextField(30);
        private final JTextField txtTipo = new JTextField(12);
        private final JTextField txtRuc = new JTextField(20);
        private final JTextField txtDireccion = new JTextField(60);
        private final JTextField txtTelefono = new JTextField(20);
        private final JTextField txtIdCentral = new JTextField(12);

        private boolean saved = false;
        private Cliente cliente;

        public ClienteFormDialog(Frame owner, Cliente c) {
            super(owner, true);
            setTitle(c == null ? "Crear Cliente" : "Editar Cliente");
            setSize(520, 360);
            setLayout(new BorderLayout());
            setLocationRelativeTo(owner);

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

            pnl.add(labeledField("ID Cliente:", txtId));
            pnl.add(labeledField("Nombre:", txtNombre));
            pnl.add(labeledField("Razón Social:", txtRazon));
            pnl.add(labeledField("Tipo Cliente:", txtTipo));
            pnl.add(labeledField("RUC:", txtRuc));
            pnl.add(labeledField("Dirección:", txtDireccion));
            pnl.add(labeledField("Teléfono:", txtTelefono));
            pnl.add(labeledField("ID Central:", txtIdCentral));

            if (c != null) {
                txtId.setText(c.getIdCliente());
                txtId.setEditable(false); // no editar PK
                txtNombre.setText(c.getNombre());
                txtRazon.setText(c.getRazonSocial());
                txtTipo.setText(c.getTipoCliente());
                txtRuc.setText(c.getRuc());
                txtDireccion.setText(c.getDireccion());
                txtTelefono.setText(c.getTelefono());
                txtIdCentral.setText(c.getIdCentral());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");

            btnSave.addActionListener(e -> {
                if (!validateFields()) return;
                cliente = new Cliente();
                cliente.setIdCliente(txtId.getText().trim());
                cliente.setNombre(txtNombre.getText().trim());
                cliente.setRazonSocial(txtRazon.getText().trim());
                cliente.setTipoCliente(txtTipo.getText().trim());
                cliente.setRuc(txtRuc.getText().trim());
                cliente.setDireccion(txtDireccion.getText().trim());
                cliente.setTelefono(txtTelefono.getText().trim());
                cliente.setIdCentral(txtIdCentral.getText().trim());
                saved = true;
                dispose();
            });

            btnCancel.addActionListener(e -> dispose());

            JPanel pnlBtns = new JPanel();
            pnlBtns.add(btnSave);
            pnlBtns.add(btnCancel);

            add(new JScrollPane(pnl), BorderLayout.CENTER);
            add(pnlBtns, BorderLayout.SOUTH);
        }

        private JPanel labeledField(String label, JComponent field) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel l = new JLabel(label);
            l.setPreferredSize(new Dimension(140, 22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID es obligatorio.");
                return false;
            }
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre es obligatorio.");
                return false;
            }
            if (txtRazon.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Razón social es obligatoria.");
                return false;
            }
            if (!txtRuc.getText().trim().matches("\\d{8,11}")) {
                JOptionPane.showMessageDialog(this, "RUC inválido (8-11 dígitos).");
                return false;
            }
            if (txtIdCentral.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe asignar una central.");
                return false;
            }
            return true;
        }

        public boolean isSaved() {
            return saved;
        }

        public Cliente getCliente() {
            return cliente;
        }
    }
}
