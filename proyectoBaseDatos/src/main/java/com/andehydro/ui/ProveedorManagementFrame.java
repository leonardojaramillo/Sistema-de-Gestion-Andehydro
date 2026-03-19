package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Proveedor;
import main.java.com.andehydro.service.ProveedorService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ProveedorManagementFrame extends JFrame {

    private Connection conn;
    private ProveedorService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    public ProveedorManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new ProveedorService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Proveedores");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Proveedor", "Nombre", "Razón Social", "RUC", "Tipo Servicio", "País"
        }, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

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
        btnVolver.addActionListener(e -> dispose());

        pnlButtons.add(btnCrear);
        pnlButtons.add(btnEditar);
        pnlButtons.add(btnEliminar);
        pnlButtons.add(btnActualizar);
        pnlButtons.add(btnVolver);

        add(sp, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);

        loadTableData();
    }

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            List<Proveedor> lista = service.listarTodos();
            for (Proveedor p : lista) {
                tableModel.addRow(new Object[] {
                        p.getIdProveedor(),
                        p.getNombre(),
                        p.getRazonSocial(),
                        p.getRuc(),
                        p.getTipoServicio(),
                        p.getPais()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando proveedores: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        ProveedorForm dialog = new ProveedorForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Proveedor p = dialog.getProveedor();
            try {
                service.crearProveedor(p);
                JOptionPane.showMessageDialog(this, "Proveedor creado.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un proveedor para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Proveedor p = service.obtenerPorId(id);
            if (p == null) { JOptionPane.showMessageDialog(this, "Proveedor no encontrado."); return; }
            ProveedorForm dialog = new ProveedorForm(this, p);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarProveedor(dialog.getProveedor());
                JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarProveedor(id);
            JOptionPane.showMessageDialog(this, "Proveedor eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class ProveedorForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtNombre = new JTextField(40);
        private final JTextField txtRazon = new JTextField(40);
        private final JTextField txtRuc = new JTextField(20);
        private final JTextField txtTipo = new JTextField(30);
        private final JTextField txtPais = new JTextField(20);

        private boolean saved = false;
        private Proveedor proveedor;

        public ProveedorForm(Frame owner, Proveedor p) {
            super(owner, true);
            setTitle(p == null ? "Crear Proveedor" : "Editar Proveedor");
            setSize(700, 380);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Proveedor:", txtId));
            pnl.add(labeledField("Nombre:", txtNombre));
            pnl.add(labeledField("Razón Social:", txtRazon));
            pnl.add(labeledField("RUC:", txtRuc));
            pnl.add(labeledField("Tipo Servicio:", txtTipo));
            pnl.add(labeledField("País:", txtPais));

            if (p != null) {
                txtId.setText(p.getIdProveedor());
                txtId.setEditable(false);
                txtNombre.setText(p.getNombre());
                txtRazon.setText(p.getRazonSocial());
                txtRuc.setText(p.getRuc());
                txtTipo.setText(p.getTipoServicio());
                txtPais.setText(p.getPais());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(a -> {
                if (!validateFields()) return;
                proveedor = new Proveedor();
                proveedor.setIdProveedor(txtId.getText().trim());
                proveedor.setNombre(txtNombre.getText().trim());
                proveedor.setRazonSocial(txtRazon.getText().trim());
                proveedor.setRuc(txtRuc.getText().trim());
                proveedor.setTipoServicio(txtTipo.getText().trim());
                proveedor.setPais(txtPais.getText().trim());
                saved = true;
                dispose();
            });
            btnCancel.addActionListener(a -> dispose());

            JPanel pnlBtns = new JPanel();
            pnlBtns.add(btnSave);
            pnlBtns.add(btnCancel);

            add(new JScrollPane(pnl), BorderLayout.CENTER);
            add(pnlBtns, BorderLayout.SOUTH);
        }

        private JPanel labeledField(String label, JComponent field) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel l = new JLabel(label);
            l.setPreferredSize(new Dimension(140,22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Nombre obligatorio"); return false; }
            if (txtRuc.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"RUC obligatorio"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Proveedor getProveedor() { return proveedor; }
    }
}
