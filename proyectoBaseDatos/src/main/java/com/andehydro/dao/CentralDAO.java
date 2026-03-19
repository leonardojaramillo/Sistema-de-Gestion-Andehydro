package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Central;
import main.java.com.andehydro.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentralDAO {

    public void insert(Central c) throws SQLException {
        String sql = "INSERT INTO CENTRAL_HIDROELECTRICA " +
                "(id_central, nombre, ubicacion, capacidad_MW, ano_inicio, tipo_planta) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getIdCentral());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getUbicacion());
            ps.setDouble(4, c.getCapacidadMW());
            ps.setInt(5, c.getAñoInicio());
            ps.setString(6, c.getTipoPlanta());

            ps.executeUpdate();
        }
    }

    public void update(Central c) throws SQLException {
        String sql = "UPDATE CENTRAL_HIDROELECTRICA SET nombre=?, ubicacion=?, capacidad_MW=?, " +
                "ano_inicio=?, tipo_planta=? WHERE id_central=?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getUbicacion());
            ps.setDouble(3, c.getCapacidadMW());
            ps.setInt(4, c.getAñoInicio());
            ps.setString(5, c.getTipoPlanta());
            ps.setString(6, c.getIdCentral());

            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM CENTRAL_HIDROELECTRICA WHERE id_central=?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Central findById(String id) throws SQLException {
        String sql = "SELECT * FROM CENTRAL_HIDROELECTRICA WHERE id_central=?";
        Central c = null;

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Central(
                        rs.getString("id_central"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getDouble("capacidad_MW"),
                        rs.getInt("ano_inicio"),
                        rs.getString("tipo_planta")
                );
            }
        }
        return c;
    }

    public List<Central> findAll() throws SQLException {
        List<Central> list = new ArrayList<>();
        String sql = "SELECT * FROM CENTRAL_HIDROELECTRICA ORDER BY id_central";

        try (Connection conn = ConnectionUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Central(
                        rs.getString("id_central"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getDouble("capacidad_MW"),
                        rs.getInt("año_inicio"),
                        rs.getString("tipo_planta")
                ));
            }
        }
        return list;
    }
}
