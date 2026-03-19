package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Compra;
import main.java.com.andehydro.service.CompraService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class CompraManagementFrame extends JFrame {

    private Connection conn;
    private CompraService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    public CompraManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new CompraService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Compras");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Compra", "Fecha", "Descripción", "Monto", "Moneda", "ID Proveedor", "ID Proyecto"
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
            List<Compra> lista = service.listarTodas();
            for (Compra c : lista) {
                tableModel.addRow(new Object[] {
                        c.getIdCompra(),
                        c.getFecha(),
                        c.getDescripcion(),
                        c.getMonto(),
                        c.getTipoMoneda(),
                        c.getIdProveedor(),
                        c.getIdProyecto()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando compras: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        CompraForm dialog = new CompraForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Compra c = dialog.getCompra();
            try {
                service.crearCompra(c);
                JOptionPane.showMessageDialog(this, "Compra creada.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una compra para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Compra c = service.obtenerPorId(id);
            if (c == null) { JOptionPane.showMessageDialog(this, "Compra no encontrada."); return; }
            CompraForm dialog = new CompraForm(this, c);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarCompra(dialog.getCompra());
                JOptionPane.showMessageDialog(this, "Compra actualizada.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una compra para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar compra " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarCompra(id);
            JOptionPane.showMessageDialog(this, "Compra eliminada.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class CompraForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtFecha = new JTextField(12);
        private final JTextField txtDescripcion = new JTextField(60);
        private final JTextField txtMonto = new JTextField(12);
        private final JTextField txtMoneda = new JTextField(6);
        private final JTextField txtIdProveedor = new JTextField(12);
        private final JTextField txtIdProyecto = new JTextField(12);

        private boolean saved = false;
        private Compra compra;

        public CompraForm(Frame owner, Compra c) {
            super(owner, true);
            setTitle(c == null ? "Crear Compra" : "Editar Compra");
            setSize(620, 360);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Compra:", txtId));
            pnl.add(labeledField("Fecha (yyyy-mm-dd):", txtFecha));
            pnl.add(labeledField("Descripción:", txtDescripcion));
            pnl.add(labeledField("Monto:", txtMonto));
            pnl.add(labeledField("Moneda:", txtMoneda));
            pnl.add(labeledField("ID Proveedor:", txtIdProveedor));
            pnl.add(labeledField("ID Proyecto:", txtIdProyecto));

            if (c != null) {
                txtId.setText(c.getIdCompra());
                txtId.setEditable(false);
                txtFecha.setText(c.getFecha() != null ? c.getFecha().toString() : "");
                txtDescripcion.setText(c.getDescripcion());
                txtMonto.setText(String.valueOf(c.getMonto()));
                txtMoneda.setText(c.getTipoMoneda());
                txtIdProveedor.setText(c.getIdProveedor());
                txtIdProyecto.setText(c.getIdProyecto());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(e -> {
                if (!validateFields()) return;
                compra = new Compra();
                compra.setIdCompra(txtId.getText().trim());
                compra.setFecha(!txtFecha.getText().trim().isEmpty() ? java.sql.Date.valueOf(txtFecha.getText().trim()) : new java.util.Date());
                compra.setDescripcion(txtDescripcion.getText().trim());
                compra.setMonto(Double.parseDouble(txtMonto.getText().trim()));
                compra.setTipoMoneda(txtMoneda.getText().trim());
                compra.setIdProveedor(txtIdProveedor.getText().trim());
                compra.setIdProyecto(txtIdProyecto.getText().trim());
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
            l.setPreferredSize(new Dimension(160, 22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            try { if (!txtMonto.getText().trim().isEmpty() && Double.parseDouble(txtMonto.getText().trim()) <= 0) { JOptionPane.showMessageDialog(this,"Monto debe ser > 0"); return false; } } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Monto inválido"); return false; }
            if (txtIdProveedor.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID proveedor obligatorio"); return false; }
            if (txtIdProyecto.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID proyecto obligatorio"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Compra getCompra() { return compra; }
    }
}
