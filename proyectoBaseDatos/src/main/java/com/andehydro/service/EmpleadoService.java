package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.EmpleadoDAO;
import main.java.com.andehydro.model.Empleado;

import java.sql.Connection;
import java.util.List;

public class EmpleadoService {

    private final Connection conn;
    private final EmpleadoDAO dao;

    public EmpleadoService(Connection conn) {
        this.conn = conn;
        this.dao = new EmpleadoDAO();
    }

    public void crearEmpleado(Empleado e) throws Exception {

        if (e == null) throw new Exception("Empleado nulo.");

        if (e.getIdEmpleado() == null || e.getIdEmpleado().isBlank())
            throw new Exception("ID del empleado es obligatorio.");

        if (e.getNombres() == null || e.getNombres().isBlank())
            throw new Exception("Nombres obligatorios.");

        if (e.getApellidos() == null || e.getApellidos().isBlank())
            throw new Exception("Apellidos obligatorios.");

        if (e.getCargo() == null || e.getCargo().isBlank())
            throw new Exception("Cargo obligatorio.");

        if (e.getArea() == null || e.getArea().isBlank())
            throw new Exception("Área obligatoria.");

        if (e.getFechaIngreso() == null)
            throw new Exception("Fecha de ingreso obligatoria.");

        if (e.getIdCentral() == null || e.getIdCentral().isBlank())
            throw new Exception("ID de central es obligatorio.");

        dao.insert(conn, e);
    }

    public void actualizarEmpleado(Empleado e) throws Exception {
        if (e == null || e.getIdEmpleado() == null)
            throw new Exception("Empleado inválido.");

        dao.update(conn, e);
    }

    public void eliminarEmpleado(String id) throws Exception {
        if (id == null || id.isBlank())
            throw new Exception("ID inválido.");

        dao.delete(conn, id);
    }

    public Empleado obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<Empleado> listarTodos() throws Exception {
        return dao.findAll(conn);
    }
}
