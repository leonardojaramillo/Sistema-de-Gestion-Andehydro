package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.ProyectoRSEDAO;
import main.java.com.andehydro.model.ProyectoRSE;

import java.util.List;

public class ProyectoRSEService {

    private final ProyectoRSEDAO dao;

    public ProyectoRSEService() {
        this.dao = new ProyectoRSEDAO();
    }

    public void crearProyecto(ProyectoRSE p) throws Exception {
        if (p == null) throw new Exception("Proyecto RSE nulo.");
        if (p.getIdProyecto() == null || p.getIdProyecto().trim().isEmpty())
            throw new Exception("ID de proyecto obligatorio.");
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            throw new Exception("Nombre obligatorio.");
        if (p.getInversion() < 0)
            throw new Exception("Inversión inválida.");

        dao.insertar(p);
    }

    public void actualizarProyecto(ProyectoRSE p) throws Exception {
        if (p == null || p.getIdProyecto() == null)
            throw new Exception("Proyecto inválido.");

        // regla de negocio
        if ("FINALIZADO".equalsIgnoreCase(p.getEstado()))
            throw new Exception("No se puede modificar un proyecto finalizado.");

        dao.actualizar(p);
    }

    public void eliminarProyecto(String id) throws Exception {
        if (id == null || id.trim().isEmpty())
            throw new Exception("ID inválido.");
        dao.eliminar(id);
    }

    public ProyectoRSE obtenerPorId(String id) throws Exception {
        return dao.obtenerPorId(id);
    }

    public List<ProyectoRSE> listarTodos() throws Exception {
        return dao.listarTodos();
    }
}
