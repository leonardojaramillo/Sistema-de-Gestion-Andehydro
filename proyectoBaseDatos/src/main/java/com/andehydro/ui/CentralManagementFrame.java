package main.java.com.andehydro.ui;

import main.java.com.andehydro.model.Central;
import main.java.com.andehydro.service.CentralService;
import main.java.com.andehydro.util.ConnectionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CentralManagementFrame extends JFrame {

    private final CentralService centralService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public CentralManagementFrame() {
        // CentralService in your code uses ConnectionUtil inside DAO so constructor no-arg
        this.centralService = new CentralService();

        setTitle("Gestión de Centrales");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "ID Central", "Nombre", "Ubicación", "Capacidad (MW)", "Año Inicio", "Tipo Planta"
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
            List<Central> lista = centralService.listarTodas();
            for (Central c : lista) {
                tableModel.addRow(new Object[] {
                        c.getIdCentral(),
                        c.getNombre(),
                        c.getUbicacion(),
                        c.getCapacidadMW(),
                        c.getAñoInicio(),
                        c.getTipoPlanta()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando centrales: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCrear() {
        CentralForm dialog = new CentralForm(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Central c = dialog.getCentral();
            try {
                centralService.crearCentral(c);
                JOptionPane.showMessageDialog(this, "Central creada correctamente.");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creando central: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una central para editar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Central c = centralService.obtenerPorId(id);
            if (c == null) { JOptionPane.showMessageDialog(this, "Central no encontrada."); return; }
            CentralForm dialog = new CentralForm(this, c);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                centralService.actualizarCentral(dialog.getCentral());
                JOptionPane.showMessageDialog(this, "Central actualizada.");
                loadTableData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar central: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una central para eliminar."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar central " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            centralService.eliminarCentral(id);
            JOptionPane.showMessageDialog(this, "Central eliminada.");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar central: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Form dialog
    private static class CentralForm extends JDialog {
        private final JTextField txtId = new JTextField(12);
        private final JTextField txtNombre = new JTextField(30);
        private final JTextField txtUbicacion = new JTextField(60);
        private final JTextField txtCapacidad = new JTextField(12);
        private final JTextField txtAno = new JTextField(6);
        private final JTextField txtTipo = new JTextField(20);

        private boolean saved = false;
        private Central central;

        public CentralForm(Frame owner, Central c) {
            super(owner, true);
            setTitle(c == null ? "Crear Central" : "Editar Central");
            setSize(520, 320);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout());

            JPanel pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
            pnl.add(labeledField("ID Central:", txtId));
            pnl.add(labeledField("Nombre:", txtNombre));
            pnl.add(labeledField("Ubicación:", txtUbicacion));
            pnl.add(labeledField("Capacidad (MW):", txtCapacidad));
            pnl.add(labeledField("Año inicio:", txtAno));
            pnl.add(labeledField("Tipo planta:", txtTipo));

            if (c != null) {
                txtId.setText(c.getIdCentral());
                txtId.setEditable(false);
                txtNombre.setText(c.getNombre());
                txtUbicacion.setText(c.getUbicacion());
                txtCapacidad.setText(String.valueOf(c.getCapacidadMW()));
                txtAno.setText(String.valueOf(c.getAñoInicio()));
                txtTipo.setText(c.getTipoPlanta());
            } else {
                txtTipo.setText("HIDRAULICA");
            }

            JButton btnSave = new JButton("Guardar");
            JButton btnCancel = new JButton("Cancelar");
            btnSave.addActionListener(e -> {
                if (!validateFields()) return;
                central = new Central();
                central.setIdCentral(txtId.getText().trim());
                central.setNombre(txtNombre.getText().trim());
                central.setUbicacion(txtUbicacion.getText().trim());
                central.setCapacidadMW(Double.parseDouble(txtCapacidad.getText().trim()));
                central.setAñoInicio(Integer.parseInt(txtAno.getText().trim()));
                central.setTipoPlanta(txtTipo.getText().trim());
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
            if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Nombre obligatorio"); return false; }
            try {
                double mw = Double.parseDouble(txtCapacidad.getText().trim());
                if (mw <= 0) { JOptionPane.showMessageDialog(this,"Capacidad debe ser > 0"); return false; }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Capacidad inválida"); return false; }
            try {
                Integer.parseInt(txtAno.getText().trim());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Año inválido"); return false; }
            return true;
        }

        public boolean isSaved() { return saved; }
        public Central getCentral() { return central; }
    }
}
