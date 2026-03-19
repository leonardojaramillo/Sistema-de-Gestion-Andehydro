package main.java.com.andehydro.dao;

import main.java.com.andehydro.model.ProyectoRSE;
import main.java.com.andehydro.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoRSEDAO {

    // INSERTAR
    public void insertar(ProyectoRSE p) throws Exception {
        String sql = "INSERT INTO PROYECTO_RSE "
                   + "(id_proyecto, nombre, descripcion, inversion, fecha_inicio, fecha_fin, estado, id_central) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIdProyecto());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getInversion());
            ps.setDate(5, p.getFechaInicio());
            ps.setDate(6, p.getFechaFin());
            ps.setString(7, p.getEstado());
            ps.setString(8, p.getIdCentral());

            ps.executeUpdate();
        }
    }

    // OBTENER POR ID
    public ProyectoRSE obtenerPorId(String id) throws Exception {
        ProyectoRSE p = null;
        String sql = "SELECT * FROM PROYECTO_RSE WHERE id_proyecto = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new ProyectoRSE(
                        rs.getString("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("inversion"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getString("estado"),
                        rs.getString("id_central")
                );
            }
        }
        return p;
    }

    // LISTAR
    public List<ProyectoRSE> listarTodos() throws Exception {
        List<ProyectoRSE> lista = new ArrayList<>();
        String sql = "SELECT * FROM PROYECTO_RSE";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ProyectoRSE(
                        rs.getString("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("inversion"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getString("estado"),
                        rs.getString("id_central")
                ));
            }
        }
        return lista;
    }

    // ACTUALIZAR
    public void actualizar(ProyectoRSE p) throws Exception {
        String sql = "UPDATE PROYECTO_RSE SET "
                   + "nombre=?, descripcion=?, inversion=?, fecha_inicio=?, fecha_fin=?, estado=?, id_central=? "
                   + "WHERE id_proyecto=?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getInversion());
            ps.setDate(4, p.getFechaInicio());
            ps.setDate(5, p.getFechaFin());
            ps.setString(6, p.getEstado());
            ps.setString(7, p.getIdCentral());
            ps.setString(8, p.getIdProyecto());

            ps.executeUpdate();
        }
    }

    // ELIMINAR
    public void eliminar(String id) throws Exception {
        String sql = "DELETE FROM PROYECTO_RSE WHERE id_proyecto=?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
