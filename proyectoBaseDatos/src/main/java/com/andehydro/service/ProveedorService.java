package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.ProveedorDAO;
import main.java.com.andehydro.model.Proveedor;

import java.sql.Connection;
import java.util.List;

public class ProveedorService {

    private final Connection conn;
    private final ProveedorDAO dao;

    public ProveedorService(Connection conn) {
        this.conn = conn;
        this.dao = new ProveedorDAO();
    }

    public void crearProveedor(Proveedor p) throws Exception {
        if (p == null) 
            throw new IllegalArgumentException("Proveedor nulo");

        if (p.getIdProveedor() == null || p.getIdProveedor().isEmpty())
            throw new Exception("ID obligatorio.");

        if (p.getRuc() == null || p.getRuc().isEmpty())
            throw new Exception("RUC obligatorio.");

        dao.insert(conn, p);
    }

    public void actualizarProveedor(Proveedor p) throws Exception {
        if (p == null || p.getIdProveedor() == null)
            throw new Exception("Proveedor inválido.");

        dao.update(conn, p);
    }

    public void eliminarProveedor(String id) throws Exception {
        if (id == null || id.isEmpty())
            throw new Exception("ID inválido.");

        dao.delete(conn, id);
    }

    public Proveedor obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<Proveedor> listarTodos() throws Exception {
        return dao.findAll(conn);
    }
}
