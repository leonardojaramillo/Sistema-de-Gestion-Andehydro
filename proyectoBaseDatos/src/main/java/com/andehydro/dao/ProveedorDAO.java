package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    // INSERT
    public void insert(Connection conn, Proveedor prov) throws Exception {
        String sql = """
                INSERT INTO PROVEEDOR
                (ID_PROVEEDOR, NOMBRE, RAZON_SOCIAL, RUC, TIPO_SERVICIO, PAIS)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prov.getIdProveedor());
            ps.setString(2, prov.getNombre());
            ps.setString(3, prov.getRazonSocial());
            ps.setString(4, prov.getRuc());
            ps.setString(5, prov.getTipoServicio());
            ps.setString(6, prov.getPais());
            ps.executeUpdate();
        }
    }

    // UPDATE
    public void update(Connection conn, Proveedor prov) throws Exception {
        String sql = """
                UPDATE PROVEEDOR SET
                NOMBRE = ?, RAZON_SOCIAL = ?, RUC = ?, TIPO_SERVICIO = ?, PAIS = ?
                WHERE ID_PROVEEDOR = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prov.getNombre());
            ps.setString(2, prov.getRazonSocial());
            ps.setString(3, prov.getRuc());
            ps.setString(4, prov.getTipoServicio());
            ps.setString(5, prov.getPais());
            ps.setString(6, prov.getIdProveedor());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(Connection conn, String id) throws Exception {
        String sql = "DELETE FROM PROVEEDOR WHERE ID_PROVEEDOR = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    // FIND BY ID
    public Proveedor findById(Connection conn, String id) throws Exception {
        String sql = "SELECT * FROM PROVEEDOR WHERE ID_PROVEEDOR = ?";
        Proveedor prov = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prov = new Proveedor(
                        rs.getString("ID_PROVEEDOR"),
                        rs.getString("NOMBRE"),
                        rs.getString("RAZON_SOCIAL"),
                        rs.getString("RUC"),
                        rs.getString("TIPO_SERVICIO"),
                        rs.getString("PAIS")
                );
            }
        }
        return prov;
    }

    // FIND ALL
    public List<Proveedor> findAll(Connection conn) throws Exception {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM PROVEEDOR";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Proveedor(
                        rs.getString("ID_PROVEEDOR"),
                        rs.getString("NOMBRE"),
                        rs.getString("RAZON_SOCIAL"),
                        rs.getString("RUC"),
                        rs.getString("TIPO_SERVICIO"),
                        rs.getString("PAIS")
                ));
            }
        }
        return lista;
    }
}
