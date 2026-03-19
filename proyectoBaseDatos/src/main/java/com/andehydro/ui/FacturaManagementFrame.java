package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Factura;
import main.java.com.andehydro.service.FacturaService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class FacturaManagementFrame extends JFrame {

    private Connection conn;
    private FacturaService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    public FacturaManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new FacturaService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Facturas");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Factura", "Fecha Emisión", "Energía (kWh)", "Monto Total", "Estado Pago", "ID Contrato"
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
            List<Factura> lista = service.listarTodas();
            for (Factura f : lista) {
                tableModel.addRow(new Object[] {
                        f.getIdFactura(),
                        f.getFechaEmision(),
                        f.getEnergiaConsumida(),
                        f.getMontoTotal(),
                        f.getEstadoPago(),
                        f.getIdContrato()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando facturas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        FacturaForm dialog = new FacturaForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Factura f = dialog.getFactura();
            try {
                service.crearFactura(f);
                JOptionPane.showMessageDialog(this, "Factura creada.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una factura para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Factura f = service.obtenerPorId(id);
            if (f == null) { JOptionPane.showMessageDialog(this, "Factura no encontrada."); return; }
            FacturaForm dialog = new FacturaForm(this, f);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarFactura(dialog.getFactura());
                JOptionPane.showMessageDialog(this, "Factura actualizada.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una factura para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar factura " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarFactura(id);
            JOptionPane.showMessageDialog(this, "Factura eliminada.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class FacturaForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtFecha = new JTextField(12);
        private final JTextField txtEnergia = new JTextField(12);
        private final JTextField txtMonto = new JTextField(12);
        private final JTextField txtEstado = new JTextField(12);
        private final JTextField txtIdContrato = new JTextField(12);

        private boolean saved = false;
        private Factura factura;

        public FacturaForm(Frame owner, Factura f) {
            super(owner, true);
            setTitle(f == null ? "Crear Factura" : "Editar Factura");
            setSize(520, 320);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Factura:", txtId));
            pnl.add(labeledField("Fecha Emisión (yyyy-mm-dd):", txtFecha));
            pnl.add(labeledField("Energía consumida:", txtEnergia));
            pnl.add(labeledField("Monto Total:", txtMonto));
            pnl.add(labeledField("Estado Pago:", txtEstado));
            pnl.add(labeledField("ID Contrato:", txtIdContrato));

            if (f != null) {
                txtId.setText(f.getIdFactura());
                txtId.setEditable(false);
                txtFecha.setText(f.getFechaEmision() != null ? f.getFechaEmision().toString() : "");
                txtEnergia.setText(String.valueOf(f.getEnergiaConsumida()));
                txtMonto.setText(String.valueOf(f.getMontoTotal()));
                txtEstado.setText(f.getEstadoPago());
                txtIdContrato.setText(f.getIdContrato());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(e -> {
                if (!validateFields()) return;
                factura = new Factura();
                factura.setIdFactura(txtId.getText().trim());
                factura.setFechaEmision(java.sql.Date.valueOf(txtFecha.getText().trim()));
                factura.setEnergiaConsumida(Double.parseDouble(txtEnergia.getText().trim()));
                factura.setMontoTotal(Double.parseDouble(txtMonto.getText().trim()));
                factura.setEstadoPago(txtEstado.getText().trim());
                factura.setIdContrato(txtIdContrato.getText().trim());
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
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtFecha.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Fecha obligatoria"); return false; }
            try { Double.parseDouble(txtEnergia.getText().trim()); } catch (Exception ex){ JOptionPane.showMessageDialog(this,"Energía inválida"); return false; }
            try { Double.parseDouble(txtMonto.getText().trim()); } catch (Exception ex){ JOptionPane.showMessageDialog(this,"Monto inválido"); return false; }
            if (txtIdContrato.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID contrato obligatorio"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Factura getFactura() { return factura; }
    }
}
