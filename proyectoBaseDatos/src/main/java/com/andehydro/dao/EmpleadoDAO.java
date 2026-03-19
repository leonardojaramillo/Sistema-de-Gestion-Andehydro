package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public void insert(Connection conn, Empleado e) throws Exception {
        String sql = "INSERT INTO EMPLEADO (id_empleado, nombres, apellidos, cargo, area, fecha_ingreso, id_jefe, id_central) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getIdEmpleado());
            ps.setString(2, e.getNombres());
            ps.setString(3, e.getApellidos());
            ps.setString(4, e.getCargo());
            ps.setString(5, e.getArea());
            ps.setDate(6, e.getFechaIngreso());
            ps.setString(7, e.getIdJefe());
            ps.setString(8, e.getIdCentral());
            ps.executeUpdate();
        }
    }

    public Empleado findById(Connection conn, String id) throws Exception {
        String sql = "SELECT * FROM EMPLEADO WHERE id_empleado = ?";
        Empleado e = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e = new Empleado(
                        rs.getString("id_empleado"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("cargo"),
                        rs.getString("area"),
                        rs.getDate("fecha_ingreso"),
                        rs.getString("id_jefe"),
                        rs.getString("id_central")
                );
            }
        }
        return e;
    }

    public List<Empleado> findAll(Connection conn) throws Exception {
        String sql = "SELECT * FROM EMPLEADO";
        List<Empleado> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getString("id_empleado"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("cargo"),
                        rs.getString("area"),
                        rs.getDate("fecha_ingreso"),
                        rs.getString("id_jefe"),
                        rs.getString("id_central")
                ));
            }
        }
        return lista;
    }

    public void update(Connection conn, Empleado e) throws Exception {
        String sql = "UPDATE EMPLEADO SET nombres=?, apellidos=?, cargo=?, area=?, fecha_ingreso=?, id_jefe=?, id_central=? " +
                     "WHERE id_empleado=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombres());
            ps.setString(2, e.getApellidos());
            ps.setString(3, e.getCargo());
            ps.setString(4, e.getArea());
            ps.setDate(5, e.getFechaIngreso());
            ps.setString(6, e.getIdJefe());
            ps.setString(7, e.getIdCentral());
            ps.setString(8, e.getIdEmpleado());
            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String id) throws Exception {
        String sql = "DELETE FROM EMPLEADO WHERE id_empleado = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
