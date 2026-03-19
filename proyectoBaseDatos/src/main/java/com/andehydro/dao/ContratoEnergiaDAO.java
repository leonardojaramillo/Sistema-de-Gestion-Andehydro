package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.ContratoEnergia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratoEnergiaDAO {

    public void insert(Connection conn, ContratoEnergia c) throws SQLException {
        String sql = "INSERT INTO CONTRATO_ENERGIA "
                   + "(id_contrato, fecha_inicio, fecha_fin, tarifa_kw, tipo_moneda, id_cliente) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getIdContrato());
            ps.setDate(2, new java.sql.Date(c.getFechaInicio().getTime()));
            ps.setDate(3, c.getFechaFin() != null ? new java.sql.Date(c.getFechaFin().getTime()) : null);
            ps.setDouble(4, c.getTarifaKw());
            ps.setString(5, c.getTipoMoneda());
            ps.setString(6, c.getIdCliente());

            ps.executeUpdate();
        }
    }

    public void update(Connection conn, ContratoEnergia c) throws SQLException {
        String sql = "UPDATE CONTRATO_ENERGIA SET "
                + "fecha_inicio=?, fecha_fin=?, tarifa_kw=?, tipo_moneda=?, id_cliente=? "
                + "WHERE id_contrato=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(c.getFechaInicio().getTime()));
            ps.setDate(2, c.getFechaFin() != null ? new java.sql.Date(c.getFechaFin().getTime()) : null);
            ps.setDouble(3, c.getTarifaKw());
            ps.setString(4, c.getTipoMoneda());
            ps.setString(5, c.getIdCliente());
            ps.setString(6, c.getIdContrato());

            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String id) throws SQLException {
        String sql = "DELETE FROM CONTRATO_ENERGIA WHERE id_contrato=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public ContratoEnergia findById(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM CONTRATO_ENERGIA WHERE id_contrato=?";
        ContratoEnergia c = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new ContratoEnergia(
                        rs.getString("id_contrato"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getDouble("tarifa_kw"),
                        rs.getString("tipo_moneda"),
                        rs.getString("id_cliente")
                );
            }
        }
        return c;
    }

    public List<ContratoEnergia> findAll(Connection conn) throws SQLException {
        List<ContratoEnergia> lista = new ArrayList<>();
        String sql = "SELECT * FROM CONTRATO_ENERGIA ORDER BY id_contrato";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new ContratoEnergia(
                        rs.getString("id_contrato"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getDouble("tarifa_kw"),
                        rs.getString("tipo_moneda"),
                        rs.getString("id_cliente")
                ));
            }
        }
        return lista;
    }
    public boolean clienteTieneContratos(Connection conn, String idCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CONTRATO_ENERGIA WHERE id_cliente = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

}
