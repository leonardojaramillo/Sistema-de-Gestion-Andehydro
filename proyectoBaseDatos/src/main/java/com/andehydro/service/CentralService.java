package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.CentralDAO;
import main.java.com.andehydro.model.Central;

import java.util.List;

public class CentralService {

    private final CentralDAO dao = new CentralDAO();

    public void crearCentral(Central c) throws Exception {
        if (c == null) throw new IllegalArgumentException("Central es nulo");
        if (c.getIdCentral() == null || c.getIdCentral().isEmpty())
            throw new Exception("El ID es obligatorio.");
        if (c.getNombre() == null || c.getNombre().trim().isEmpty())
            throw new Exception("El nombre es obligatorio.");
        if (c.getCapacidadMW() <= 0)
            throw new Exception("La capacidad debe ser mayor a 0.");

        dao.insert(c);
    }

    public void actualizarCentral(Central c) throws Exception {
        if (c == null) throw new IllegalArgumentException("Central es nulo");
        if (c.getIdCentral() == null || c.getIdCentral().isEmpty())
            throw new Exception("El ID es obligatorio.");

        dao.update(c);
    }

    public void eliminarCentral(String id) throws Exception {
        if (id == null || id.trim().isEmpty())
            throw new Exception("ID inválido.");

        dao.delete(id);
    }

    public Central obtenerPorId(String id) throws Exception {
        if (id == null || id.trim().isEmpty())
            throw new Exception("ID inválido.");
        return dao.findById(id);
    }

    public List<Central> listarTodas() throws Exception {
        return dao.findAll();
    }
}
