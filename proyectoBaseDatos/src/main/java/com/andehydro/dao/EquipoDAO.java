package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    // INSERT
    public void insert(Connection conn, Equipo e) throws Exception {
        String sql = "INSERT INTO EQUIPO (id_equipo, tipo_equipo, marca, modelo, fecha_instalacion, estado, id_central) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getIdEquipo());
            ps.setString(2, e.getTipoEquipo());
            ps.setString(3, e.getMarca());
            ps.setString(4, e.getModelo());
            ps.setDate(5, e.getFechaInstalacion());
            ps.setString(6, e.getEstado());
            ps.setString(7, e.getIdCentral());

            ps.executeUpdate();
        }
    }

    // FIND BY ID
    public Equipo findById(Connection conn, String id) throws Exception {
        String sql = "SELECT * FROM EQUIPO WHERE id_equipo = ?";
        Equipo e = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e = new Equipo(
                    rs.getString("id_equipo"),
                    rs.getString("tipo_equipo"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getDate("fecha_instalacion"),
                    rs.getString("estado"),
                    rs.getString("id_central")
                );
            }
        }
        return e;
    }

    // LIST ALL
    public List<Equipo> findAll(Connection conn) throws Exception {
        String sql = "SELECT * FROM EQUIPO";
        List<Equipo> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Equipo(
                    rs.getString("id_equipo"),
                    rs.getString("tipo_equipo"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getDate("fecha_instalacion"),
                    rs.getString("estado"),
                    rs.getString("id_central")
                ));
            }
        }
        return lista;
    }

    // UPDATE
    public void update(Connection conn, Equipo e) throws Exception {
        String sql = "UPDATE EQUIPO SET tipo_equipo=?, marca=?, modelo=?, fecha_instalacion=?, estado=?, id_central=? "
                   + "WHERE id_equipo=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTipoEquipo());
            ps.setString(2, e.getMarca());
            ps.setString(3, e.getModelo());
            ps.setDate(4, e.getFechaInstalacion());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getIdCentral());
            ps.setString(7, e.getIdEquipo());

            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(Connection conn, String id) throws Exception {
        String sql = "DELETE FROM EQUIPO WHERE id_equipo = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
