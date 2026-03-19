package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public void insert(Connection conn, Compra c) throws SQLException {
        String sql = "INSERT INTO COMPRA (id_compra, fecha, descripcion, monto, tipo_moneda, id_proveedor, id_proyecto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getIdCompra());
            ps.setDate(2, new java.sql.Date(c.getFecha().getTime()));
            ps.setString(3, c.getDescripcion());
            ps.setDouble(4, c.getMonto());
            ps.setString(5, c.getTipoMoneda());
            ps.setString(6, c.getIdProveedor());
            ps.setString(7, c.getIdProyecto());

            ps.executeUpdate();
        }
    }

    public void update(Connection conn, Compra c) throws SQLException {
        String sql = "UPDATE COMPRA SET fecha=?, descripcion=?, monto=?, tipo_moneda=?, id_proveedor=?, id_proyecto=? "
                + "WHERE id_compra=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(c.getFecha().getTime()));
            ps.setString(2, c.getDescripcion());
            ps.setDouble(3, c.getMonto());
            ps.setString(4, c.getTipoMoneda());
            ps.setString(5, c.getIdProveedor());
            ps.setString(6, c.getIdProyecto());
            ps.setString(7, c.getIdCompra());

            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String id) throws SQLException {
        String sql = "DELETE FROM COMPRA WHERE id_compra=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Compra findById(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM COMPRA WHERE id_compra=?";
        Compra c = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Compra(
                        rs.getString("id_compra"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getString("tipo_moneda"),
                        rs.getString("id_proveedor"),
                        rs.getString("id_proyecto")
                );
            }
        }
        return c;
    }

    public List<Compra> findAll(Connection conn) throws SQLException {
        List<Compra> list = new ArrayList<>();
        String sql = "SELECT * FROM COMPRA ORDER BY id_compra";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Compra(
                        rs.getString("id_compra"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getString("tipo_moneda"),
                        rs.getString("id_proveedor"),
                        rs.getString("id_proyecto")
                ));
            }
        }
        return list;
    }
}
