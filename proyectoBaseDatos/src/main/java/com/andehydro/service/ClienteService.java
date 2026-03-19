package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.ClienteDAO;
import main.java.com.andehydro.dao.ContratoEnergiaDAO;
import main.java.com.andehydro.model.Cliente;

import java.sql.Connection;
import java.util.List;

public class ClienteService {

    private final Connection conn;
    private final ClienteDAO clienteDAO;
    private final ContratoEnergiaDAO contratoDAO;

    public ClienteService(Connection conn) {
        this.conn = conn;
        this.clienteDAO = new ClienteDAO();
        this.contratoDAO = new ContratoEnergiaDAO();
    }

    public void crearCliente(Cliente c) throws Exception {

        if (c == null) throw new Exception("Cliente nulo");

        if (c.getIdCliente() == null || c.getIdCliente().isEmpty())
            throw new Exception("El ID del cliente es obligatorio");

        if (c.getNombre() == null || c.getNombre().isEmpty())
            throw new Exception("El nombre es obligatorio");

        if (c.getRazonSocial() == null || c.getRazonSocial().isEmpty())
            throw new Exception("La razón social es obligatoria");

        if (c.getTipoCliente() == null || c.getTipoCliente().isEmpty())
            throw new Exception("El tipo de cliente es obligatorio");

        if (c.getRuc() == null || !c.getRuc().matches("\\d{8,11}"))
            throw new Exception("RUC inválido (solo dígitos, 8–11)");

        if (c.getIdCentral() == null || c.getIdCentral().isEmpty())
            throw new Exception("Debe asignar una central hidroeléctrica");

        clienteDAO.insert(conn, c);
    }

    public void actualizarCliente(Cliente c) throws Exception {
        if (c == null || c.getIdCliente() == null || c.getIdCliente().isEmpty())
            throw new Exception("Cliente o ID inválido.");

        clienteDAO.update(conn, c);
    }

    public void eliminarCliente(String idCliente) throws Exception {

        if (idCliente == null || idCliente.isEmpty())
            throw new Exception("ID inválido.");

        boolean tieneContratos = contratoDAO.clienteTieneContratos(conn, idCliente);

        if (tieneContratos)
            throw new Exception("El cliente tiene contratos asociados y no puede eliminarse.");

        clienteDAO.delete(conn, idCliente);
    }

    public Cliente obtenerPorId(String id) throws Exception {
        return clienteDAO.findById(conn, id);
    }

    public List<Cliente> listarTodos() throws Exception {
        return clienteDAO.findAll(conn);
    }
}
