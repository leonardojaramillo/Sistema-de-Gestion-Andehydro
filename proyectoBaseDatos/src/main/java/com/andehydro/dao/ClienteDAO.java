package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void insert(Connection conn, Cliente c) throws SQLException {
        String sql = "INSERT INTO CLIENTE (id_cliente, nombre, razon_social, tipo_cliente, ruc, direccion, telefono, id_central) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getRazonSocial());
            ps.setString(4, c.getTipoCliente());
            ps.setString(5, c.getRuc());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getTelefono());
            ps.setString(8, c.getIdCentral());

            ps.executeUpdate();
        }
    }

    public void update(Connection conn, Cliente c) throws SQLException {

        String sql = "UPDATE CLIENTE SET nombre=?, razon_social=?, tipo_cliente=?, ruc=?, direccion=?, telefono=?, id_central=? "
                   + "WHERE id_cliente=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRazonSocial());
            ps.setString(3, c.getTipoCliente());
            ps.setString(4, c.getRuc());
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getTelefono());
            ps.setString(7, c.getIdCentral());
            ps.setString(8, c.getIdCliente());

            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String idCliente) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE id_cliente=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCliente);
            ps.executeUpdate();
        }
    }

    public Cliente findById(Connection conn, String idCliente) throws SQLException {

        String sql = "SELECT * FROM CLIENTE WHERE id_cliente=?";
        Cliente c = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Cliente(
                        rs.getString("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("razon_social"),
                        rs.getString("tipo_cliente"),
                        rs.getString("ruc"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("id_central")
                );
            }
        }
        return c;
    }

    public List<Cliente> findAll(Connection conn) throws SQLException {

        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE ORDER BY id_cliente";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("razon_social"),
                        rs.getString("tipo_cliente"),
                        rs.getString("ruc"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("id_central")
                ));
            }
        }
        return lista;
    }

}
