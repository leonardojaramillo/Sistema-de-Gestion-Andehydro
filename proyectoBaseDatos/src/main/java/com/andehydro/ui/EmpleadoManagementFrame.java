package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Empleado;
import main.java.com.andehydro.service.EmpleadoService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class EmpleadoManagementFrame extends JFrame {

    private Connection conn;
    private EmpleadoService service;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    public EmpleadoManagementFrame() {
        try {
            conn = ConnectionUtil.getConnection();
            service = new EmpleadoService(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Empleados");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Empleado", "Nombres", "Apellidos", "Cargo", "Área", "Fecha Ingreso", "ID Jefe", "ID Central"
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
            List<Empleado> lista = service.listarTodos();
            for (Empleado e : lista) {
                tableModel.addRow(new Object[] {
                        e.getIdEmpleado(),
                        e.getNombres(),
                        e.getApellidos(),
                        e.getCargo(),
                        e.getArea(),
                        e.getFechaIngreso(),
                        e.getIdJefe(),
                        e.getIdCentral()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando empleados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        EmpleadoForm dialog = new EmpleadoForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Empleado emp = dialog.getEmpleado();
            try {
                service.crearEmpleado(emp);
                JOptionPane.showMessageDialog(this, "Empleado creado.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un empleado para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Empleado e = service.obtenerPorId(id);
            if (e == null) { JOptionPane.showMessageDialog(this, "Empleado no encontrado."); return; }
            EmpleadoForm dialog = new EmpleadoForm(this, e);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarEmpleado(dialog.getEmpleado());
                JOptionPane.showMessageDialog(this, "Empleado actualizado.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar empleado " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarEmpleado(id);
            JOptionPane.showMessageDialog(this, "Empleado eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception ignored) {}
    }

    private static class EmpleadoForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtNombres = new JTextField(30);
        private final JTextField txtApellidos = new JTextField(30);
        private final JTextField txtCargo = new JTextField(20);
        private final JTextField txtArea = new JTextField(20);
        private final JTextField txtFecha = new JTextField(12);
        private final JTextField txtIdJefe = new JTextField(12);
        private final JTextField txtIdCentral = new JTextField(12);

        private boolean saved = false;
        private Empleado empleado;

        public EmpleadoForm(Frame owner, Empleado e) {
            super(owner, true);
            setTitle(e == null ? "Crear Empleado" : "Editar Empleado");
            setSize(700, 420);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Empleado:", txtId));
            pnl.add(labeledField("Nombres:", txtNombres));
            pnl.add(labeledField("Apellidos:", txtApellidos));
            pnl.add(labeledField("Cargo:", txtCargo));
            pnl.add(labeledField("Área:", txtArea));
            pnl.add(labeledField("Fecha Ingreso (yyyy-mm-dd):", txtFecha));
            pnl.add(labeledField("ID Jefe (opcional):", txtIdJefe));
            pnl.add(labeledField("ID Central:", txtIdCentral));

            if (e != null) {
                txtId.setText(e.getIdEmpleado());
                txtId.setEditable(false);
                txtNombres.setText(e.getNombres());
                txtApellidos.setText(e.getApellidos());
                txtCargo.setText(e.getCargo());
                txtArea.setText(e.getArea());
                txtFecha.setText(e.getFechaIngreso() != null ? e.getFechaIngreso().toString() : "");
                txtIdJefe.setText(e.getIdJefe());
                txtIdCentral.setText(e.getIdCentral());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(a -> {
                if (!validateFields()) return;
                empleado = new Empleado();
                empleado.setIdEmpleado(txtId.getText().trim());
                empleado.setNombres(txtNombres.getText().trim());
                empleado.setApellidos(txtApellidos.getText().trim());
                empleado.setCargo(txtCargo.getText().trim());
                empleado.setArea(txtArea.getText().trim());
                empleado.setFechaIngreso(!txtFecha.getText().trim().isEmpty() ? java.sql.Date.valueOf(txtFecha.getText().trim()) : null);
                empleado.setIdJefe(txtIdJefe.getText().trim().isEmpty() ? null : txtIdJefe.getText().trim());
                empleado.setIdCentral(txtIdCentral.getText().trim());
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
            l.setPreferredSize(new Dimension(160, 22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtNombres.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Nombres obligatorios"); return false; }
            if (txtApellidos.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Apellidos obligatorios"); return false; }
            if (txtCargo.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Cargo obligatorio"); return false; }
            if (txtArea.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Área obligatorio"); return false; }
            if (txtIdCentral.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID Central obligatorio"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Empleado getEmpleado() { return empleado; }
    }
}
