package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Equipo;
import main.java.com.andehydro.service.EquipoService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class EquipoManagementFrame extends JFrame {

    private Connection conn;
    private EquipoService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    public EquipoManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new EquipoService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Equipos");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Equipo", "Tipo", "Marca", "Modelo", "Fecha Instalación", "Estado", "ID Central"
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
            List<Equipo> lista = service.listarTodos();
            for (Equipo e : lista) {
                tableModel.addRow(new Object[] {
                        e.getIdEquipo(),
                        e.getTipoEquipo(),
                        e.getMarca(),
                        e.getModelo(),
                        e.getFechaInstalacion(),
                        e.getEstado(),
                        e.getIdCentral()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando equipos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        EquipoForm dialog = new EquipoForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Equipo eq = dialog.getEquipo();
            try {
                service.crearEquipo(eq);
                JOptionPane.showMessageDialog(this, "Equipo creado.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un equipo para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Equipo e = service.obtenerPorId(id);
            if (e == null) { JOptionPane.showMessageDialog(this, "Equipo no encontrado."); return; }
            EquipoForm dialog = new EquipoForm(this, e);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarEquipo(dialog.getEquipo());
                JOptionPane.showMessageDialog(this, "Equipo actualizado.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un equipo para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar equipo " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarEquipo(id);
            JOptionPane.showMessageDialog(this, "Equipo eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class EquipoForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtTipo = new JTextField(20);
        private final JTextField txtMarca = new JTextField(20);
        private final JTextField txtModelo = new JTextField(20);
        private final JTextField txtFecha = new JTextField(12);
        private final JTextField txtEstado = new JTextField(20);
        private final JTextField txtIdCentral = new JTextField(12);

        private boolean saved = false;
        private Equipo equipo;

        public EquipoForm(Frame owner, Equipo e) {
            super(owner, true);
            setTitle(e == null ? "Crear Equipo" : "Editar Equipo");
            setSize(620, 360);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Equipo:", txtId));
            pnl.add(labeledField("Tipo Equipo:", txtTipo));
            pnl.add(labeledField("Marca:", txtMarca));
            pnl.add(labeledField("Modelo:", txtModelo));
            pnl.add(labeledField("Fecha Instalación (yyyy-mm-dd):", txtFecha));
            pnl.add(labeledField("Estado:", txtEstado));
            pnl.add(labeledField("ID Central:", txtIdCentral));

            if (e != null) {
                txtId.setText(e.getIdEquipo());
                txtId.setEditable(false);
                txtTipo.setText(e.getTipoEquipo());
                txtMarca.setText(e.getMarca());
                txtModelo.setText(e.getModelo());
                txtFecha.setText(e.getFechaInstalacion() != null ? e.getFechaInstalacion().toString() : "");
                txtEstado.setText(e.getEstado());
                txtIdCentral.setText(e.getIdCentral());
            } else {
                txtEstado.setText("Operativo");
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(a -> {
                if (!validateFields()) return;
                equipo = new Equipo();
                equipo.setIdEquipo(txtId.getText().trim());
                equipo.setTipoEquipo(txtTipo.getText().trim());
                equipo.setMarca(txtMarca.getText().trim());
                equipo.setModelo(txtModelo.getText().trim());
                equipo.setFechaInstalacion(!txtFecha.getText().trim().isEmpty() ? java.sql.Date.valueOf(txtFecha.getText().trim()) : null);
                equipo.setEstado(txtEstado.getText().trim());
                equipo.setIdCentral(txtIdCentral.getText().trim());
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
            l.setPreferredSize(new Dimension(160,22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtTipo.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Tipo obligatorio"); return false; }
            if (txtMarca.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Marca obligatorio"); return false; }
            if (txtModelo.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Modelo obligatorio"); return false; }
            if (txtIdCentral.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID Central obligatorio"); return false; }
            if (!txtEstado.getText().matches("Operativo|Mantenimiento|Inactivo")) {
                JOptionPane.showMessageDialog(this, "Estado inválido (Operativo/Mantenimiento/Inactivo)");
                return false;
            }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Equipo getEquipo() { return equipo; }
    }
}
