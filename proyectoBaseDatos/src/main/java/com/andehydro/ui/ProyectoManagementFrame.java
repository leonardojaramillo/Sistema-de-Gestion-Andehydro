package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.ProyectoRSE;
import main.java.com.andehydro.service.ProyectoRSEService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProyectoManagementFrame extends JFrame {

    private final ProyectoRSEService service;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ProyectoManagementFrame() {
        this.service = new ProyectoRSEService(); // tu servicio no requiere Connection en la versión que diste

        setTitle("Gestión de Proyectos RSE");
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Proyecto", "Nombre", "Descripción", "Inversión", "Fecha Inicio", "Fecha Fin", "Estado", "ID Central"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
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
            List<ProyectoRSE> lista = service.listarTodos();
            for (ProyectoRSE p : lista) {
                tableModel.addRow(new Object[] {
                        p.getIdProyecto(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getInversion(),
                        p.getFechaInicio(),
                        p.getFechaFin(),
                        p.getEstado(),
                        p.getIdCentral()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando proyectos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        ProyectoForm dialog = new ProyectoForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            ProyectoRSE p = dialog.getProyecto();
            try {
                service.crearProyecto(p);
                JOptionPane.showMessageDialog(this, "Proyecto creado.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando proyecto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un proyecto para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            ProyectoRSE p = service.obtenerPorId(id);
            if (p == null) { JOptionPane.showMessageDialog(this, "Proyecto no encontrado."); return; }
            ProyectoForm dialog = new ProyectoForm(this, p);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                service.actualizarProyecto(dialog.getProyecto());
                JOptionPane.showMessageDialog(this, "Proyecto actualizado.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar proyecto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un proyecto para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar proyecto " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            service.eliminarProyecto(id);
            JOptionPane.showMessageDialog(this, "Proyecto eliminado.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar proyecto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Form dialog
    private static class ProyectoForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtNombre = new JTextField(40);
        private final JTextArea txtDescripcion = new JTextArea(4, 40);
        private final JTextField txtInversion = new JTextField(15);
        private final JTextField txtFechaInicio = new JTextField(12);
        private final JTextField txtFechaFin = new JTextField(12);
        private final JTextField txtEstado = new JTextField(20);
        private final JTextField txtIdCentral = new JTextField(12);

        private boolean saved = false;
        private ProyectoRSE proyecto;

        public ProyectoForm(Frame owner, ProyectoRSE p) {
            super(owner, true);
            setTitle(p == null ? "Crear Proyecto" : "Editar Proyecto");
            setSize(700, 420);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Proyecto:", txtId));
            pnl.add(labeledField("Nombre:", txtNombre));
            pnl.add(labeledArea("Descripción:", txtDescripcion));
            pnl.add(labeledField("Inversión:", txtInversion));
            pnl.add(labeledField("Fecha Inicio (yyyy-mm-dd):", txtFechaInicio));
            pnl.add(labeledField("Fecha Fin (yyyy-mm-dd, opcional):", txtFechaFin));
            pnl.add(labeledField("Estado:", txtEstado));
            pnl.add(labeledField("ID Central:", txtIdCentral));

            if (p != null) {
                txtId.setText(p.getIdProyecto());
                txtId.setEditable(false);
                txtNombre.setText(p.getNombre());
                txtDescripcion.setText(p.getDescripcion());
                txtInversion.setText(String.valueOf(p.getInversion()));
                txtFechaInicio.setText(p.getFechaInicio() != null ? p.getFechaInicio().toString() : "");
                txtFechaFin.setText(p.getFechaFin() != null ? p.getFechaFin().toString() : "");
                txtEstado.setText(p.getEstado());
                txtIdCentral.setText(p.getIdCentral());
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(e -> {
                if (!validateFields()) return;
                proyecto = new ProyectoRSE();
                proyecto.setIdProyecto(txtId.getText().trim());
                proyecto.setNombre(txtNombre.getText().trim());
                proyecto.setDescripcion(txtDescripcion.getText().trim());
                proyecto.setInversion(Double.parseDouble(txtInversion.getText().trim()));
                proyecto.setFechaInicio(java.sql.Date.valueOf(txtFechaInicio.getText().trim()));
                if (!txtFechaFin.getText().trim().isEmpty())
                    proyecto.setFechaFin(java.sql.Date.valueOf(txtFechaFin.getText().trim()));
                proyecto.setEstado(txtEstado.getText().trim());
                proyecto.setIdCentral(txtIdCentral.getText().trim());
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
            l.setPreferredSize(new Dimension(170, 22));
            p.add(l, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return p;
        }

        private JPanel labeledArea(String label, JTextArea area) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel l = new JLabel(label);
            l.setPreferredSize(new Dimension(170, 22));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            p.add(l, BorderLayout.WEST);
            p.add(new JScrollPane(area), BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return p;
        }

        private boolean validateFields() {
            if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID obligatorio"); return false; }
            if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Nombre obligatorio"); return false; }
            try {
                double inv = Double.parseDouble(txtInversion.getText().trim());
                if (inv < 0) { JOptionPane.showMessageDialog(this,"Inversión inválida"); return false; }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Inversión inválida"); return false; }
            if (txtFechaInicio.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Fecha inicio obligatorio"); return false; }
            if (txtEstado.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Estado obligatorio"); return false; }
            if (txtIdCentral.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID Central obligatorio"); return false; }
            // date format validation omitted for brevity; service will also validate
            return true;
        }

        public boolean isSaved() { return saved; }
        public ProyectoRSE getProyecto() { return proyecto; }
    }
}
