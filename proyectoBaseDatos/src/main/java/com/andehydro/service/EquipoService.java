package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.EquipoDAO;
import main.java.com.andehydro.model.Equipo;

import java.sql.Connection;
import java.util.List;

public class EquipoService {

    private final Connection conn;
    private final EquipoDAO dao;

    public EquipoService(Connection conn) {
        this.conn = conn;
        this.dao = new EquipoDAO();
    }

    public void crearEquipo(Equipo e) throws Exception {

        if (e == null) throw new Exception("Equipo nulo.");

        if (e.getIdEquipo() == null || e.getIdEquipo().isBlank())
            throw new Exception("ID del equipo obligatorio.");

        if (e.getTipoEquipo() == null || e.getTipoEquipo().isBlank())
            throw new Exception("Tipo de equipo obligatorio.");

        if (e.getMarca() == null || e.getMarca().isBlank())
            throw new Exception("Marca obligatoria.");

        if (e.getModelo() == null || e.getModelo().isBlank())
            throw new Exception("Modelo obligatorio.");

        if (e.getFechaInstalacion() == null)
            throw new Exception("Fecha de instalación obligatoria.");

        if (e.getIdCentral() == null || e.getIdCentral().isBlank())
            throw new Exception("ID de central obligatorio.");

        // Validar estado según la tabla
        if (e.getEstado() == null || e.getEstado().isBlank())
            e.setEstado("Operativo");

        if (!e.getEstado().matches("Operativo|Mantenimiento|Inactivo"))
            throw new Exception("Estado inválido para EQUIPO.");

        dao.insert(conn, e);
    }

    public void actualizarEquipo(Equipo e) throws Exception {
        if (e == null || e.getIdEquipo() == null)
            throw new Exception("Equipo inválido.");

        if (!e.getEstado().matches("Operativo|Mantenimiento|Inactivo"))
            throw new Exception("Estado inválido.");

        dao.update(conn, e);
    }

    public void eliminarEquipo(String id) throws Exception {
        if (id == null || id.isBlank())
            throw new Exception("ID inválido.");

        dao.delete(conn, id);
    }

    public Equipo obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<Equipo> listarTodos() throws Exception {
        return dao.findAll(conn);
    }
}
