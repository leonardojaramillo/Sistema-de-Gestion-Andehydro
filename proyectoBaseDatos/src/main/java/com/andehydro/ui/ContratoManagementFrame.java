package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.ContratoEnergia;
import main.java.com.andehydro.service.ContratoEnergiaService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ContratoManagementFrame extends JFrame {

    private Connection conn;
    private ContratoEnergiaService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);;

    public ContratoManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new ContratoEnergiaService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Contratos");
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Contrato", "Fecha Inicio", "Fecha Fin", "Tarifa kW", "Moneda", "ID Cliente"
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
            List<ContratoEnergia> lista = service.listarTodos();
            for (ContratoEnergia c : lista) {
                tableModel.addRow(new Object[] {
                        c.getIdContrato(),
                        c.getFechaInicio(),
                        c.getFechaFin(),
                        c.getTarifaKw(),
                        c.getTipoMoneda(),
                        c.getIdCliente()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando contratos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        ContratoForm dialog = new ContratoForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            ContratoEnergia c = dialog.getContrato();
            try {
                service.crearContrato(c);
                JOptionPane.showMessageDialog(this, "Contrato creado.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando contrato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un contrato para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            ContratoEnergia c = service.obtenerPorId(id);
            if (c == null) { JOptionPane.showMessageDialog(this, "Contrato no encontrado."); return; }
            ContratoForm dialog = new ContratoForm(this, c);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarContrato(dialog.getContrato());
                JOptionPane.showMessageDialog(this, "Contrato actualizado.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar contrato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un contrato para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar contrato " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarContrato(id);
            JOptionPane.showMessageDialog(this, "Contrato eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar contrato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class ContratoForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtFechaInicio = new JTextField(12);
        private final JTextField txtFechaFin = new JTextField(12);
        private final JTextField txtTarifa = new JTextField(12);
        private final JTextField txtMoneda = new JTextField(6);
        private final JTextField txtIdCliente = new JTextField(12);

        private boolean saved = false;
        private ContratoEnergia contrato;

        public ContratoForm(Frame owner, ContratoEnergia c) {
            super(owner, true);
            setTitle(c == null ? "Crear Contrato" : "Editar Contrato");
            setSize(620, 360);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Contrato:", txtId));
            pnl.add(labeledField("Fecha Inicio (yyyy-mm-dd):", txtFechaInicio));
            pnl.add(labeledField("Fecha Fin (yyyy-mm-dd, opcional):", txtFechaFin));
            pnl.add(labeledField("Tarifa kW:", txtTarifa));
            pnl.add(labeledField("Tipo Moneda (USD/PEN/EUR):", txtMoneda));
            pnl.add(labeledField("ID Cliente:", txtIdCliente));

            if (c != null) {
                txtId.setText(c.getIdContrato());
                txtId.setEditable(false);
                txtFechaInicio.setText(c.getFechaInicio() != null ? c.getFechaInicio().toString() : "");
                txtFechaFin.setText(c.getFechaFin() != null ? c.getFechaFin().toString() : "");
                txtTarifa.setText(String.valueOf(c.getTarifaKw()));
                txtMoneda.setText(c.getTipoMoneda());
                txtIdCliente.setText(c.getIdCliente());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(a -> {
                if (!validateFields()) return;
                contrato = new ContratoEnergia();
                contrato.setIdContrato(txtId.getText().trim());
                contrato.setFechaInicio(java.sql.Date.valueOf(txtFechaInicio.getText().trim()));
                if (!txtFechaFin.getText().trim().isEmpty())
                    contrato.setFechaFin(java.sql.Date.valueOf(txtFechaFin.getText().trim()));
                contrato.setTarifaKw(Double.parseDouble(txtTarifa.getText().trim()));
                contrato.setTipoMoneda(txtMoneda.getText().trim());
                contrato.setIdCliente(txtIdCliente.getText().trim());
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
            l.setPreferredSize(new Dimension(180,22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtFechaInicio.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Fecha inicio obligatorio"); return false; }
            try { Double.parseDouble(txtTarifa.getText().trim()); } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Tarifa inválida"); return false; }
            if (!txtMoneda.getText().matches("USD|PEN|EUR")) { JOptionPane.showMessageDialog(this,"Moneda inválida (USD/PEN/EUR)"); return false; }
            if (txtIdCliente.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID cliente obligatorio"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public ContratoEnergia getContrato() { return contrato; }
    }
}
