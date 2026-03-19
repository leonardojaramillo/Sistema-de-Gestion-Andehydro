package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.ContratoEnergiaDAO;
import main.java.com.andehydro.model.ContratoEnergia;

import java.sql.Connection;
import java.util.List;

public class ContratoEnergiaService {

    private final Connection conn;
    private final ContratoEnergiaDAO dao;

    public ContratoEnergiaService(Connection conn) {
        this.conn = conn;
        this.dao = new ContratoEnergiaDAO();
    }

    public void crearContrato(ContratoEnergia c) throws Exception {
        if (c == null) throw new IllegalArgumentException("Contrato nulo");

        if (c.getIdContrato() == null || c.getIdContrato().trim().isEmpty())
            throw new Exception("ID de contrato obligatorio.");

        if (c.getFechaInicio() == null)
            throw new Exception("Fecha de inicio obligatoria.");

        if (c.getFechaFin() != null && c.getFechaFin().before(c.getFechaInicio()))
            throw new Exception("Fecha fin no puede ser anterior a fecha inicio.");

        if (c.getTarifaKw() <= 0)
            throw new Exception("Tarifa por kW debe ser mayor a 0.");

        if (!c.getTipoMoneda().matches("USD|PEN|EUR"))
            throw new Exception("Tipo de moneda inválido (USD, PEN, EUR).");

        dao.insert(conn, c);
    }

    public void actualizarContrato(ContratoEnergia c) throws Exception {
        if (c == null || c.getIdContrato() == null)
            throw new Exception("Contrato inválido.");

        if (c.getFechaFin() != null && c.getFechaFin().before(c.getFechaInicio()))
            throw new Exception("Fecha fin no puede ser anterior a fecha inicio.");

        dao.update(conn, c);
    }

    public void eliminarContrato(String id) throws Exception {
        if (id == null || id.trim().isEmpty())
            throw new Exception("ID inválido.");

        dao.delete(conn, id);
    }

    public ContratoEnergia obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<ContratoEnergia> listarTodos() throws Exception {
        return dao.findAll(conn);
    }
}
