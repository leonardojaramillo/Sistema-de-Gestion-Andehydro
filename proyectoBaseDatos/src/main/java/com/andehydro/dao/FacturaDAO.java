package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // INSERT
    public void insert(Connection conn, Factura factura) throws Exception {
        String sql = """
                INSERT INTO FACTURACION
                (ID_FACTURA, FECHA_EMISION, ENERGIA_CONSUMIDA, MONTO_TOTAL, ESTADO_PAGO, ID_CONTRATO)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, factura.getIdFactura());
            ps.setDate(2, factura.getFechaEmision());
            ps.setDouble(3, factura.getEnergiaConsumida());
            ps.setDouble(4, factura.getMontoTotal());
            ps.setString(5, factura.getEstadoPago());
            ps.setString(6, factura.getIdContrato());
            ps.executeUpdate();
        }
    }

    // UPDATE
    public void update(Connection conn, Factura factura) throws Exception {
        String sql = """
                UPDATE FACTURACION SET
                FECHA_EMISION = ?, ENERGIA_CONSUMIDA = ?, MONTO_TOTAL = ?, ESTADO_PAGO = ?, ID_CONTRATO = ?
                WHERE ID_FACTURA = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, factura.getFechaEmision());
            ps.setDouble(2, factura.getEnergiaConsumida());
            ps.setDouble(3, factura.getMontoTotal());
            ps.setString(4, factura.getEstadoPago());
            ps.setString(5, factura.getIdContrato());
            ps.setString(6, factura.getIdFactura());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(Connection conn, String id) throws Exception {
        String sql = "DELETE FROM FACTURACION WHERE ID_FACTURA = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    // FIND BY ID
    public Factura findById(Connection conn, String id) throws Exception {
        String sql = "SELECT * FROM FACTURACION WHERE ID_FACTURA = ?";
        Factura f = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                f = new Factura(
                        rs.getString("ID_FACTURA"),
                        rs.getDate("FECHA_EMISION"),
                        rs.getDouble("ENERGIA_CONSUMIDA"),
                        rs.getDouble("MONTO_TOTAL"),
                        rs.getString("ESTADO_PAGO"),
                        rs.getString("ID_CONTRATO")
                );
            }
        }
        return f;
    }

    // FIND ALL
    public List<Factura> findAll(Connection conn) throws Exception {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM FACTURACION ORDER BY FECHA_EMISION DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Factura(
                        rs.getString("ID_FACTURA"),
                        rs.getDate("FECHA_EMISION"),
                        rs.getDouble("ENERGIA_CONSUMIDA"),
                        rs.getDouble("MONTO_TOTAL"),
                        rs.getString("ESTADO_PAGO"),
                        rs.getString("ID_CONTRATO")
                ));
            }
        }
        return lista;
    }
}
