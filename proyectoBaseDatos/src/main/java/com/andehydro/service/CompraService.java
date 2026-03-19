package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.CompraDAO;
import main.java.com.andehydro.model.Compra;

import java.sql.Connection;
import java.util.List;

public class CompraService {

    private final Connection conn;
    private final CompraDAO dao;

    public CompraService(Connection conn) {
        this.conn = conn;
        this.dao = new CompraDAO();
    }

    public void crearCompra(Compra c) throws Exception {
        if (c == null) throw new IllegalArgumentException("Compra nula");
        if (c.getIdCompra() == null || c.getIdCompra().trim().isEmpty())
            throw new Exception("ID de compra es obligatorio.");
        if (c.getMonto() <= 0) throw new Exception("El monto debe ser mayor a 0.");
        if (c.getMonto() > 100000 && (c.getDescripcion() == null || c.getDescripcion().trim().isEmpty()))
            throw new Exception("Compras mayores a 100,000 requieren descripción obligatoria.");
        if (c.getFecha() == null) c.setFecha(new java.util.Date());

        dao.insert(conn, c);
    }

    public void actualizarCompra(Compra c) throws Exception {
        if (c == null || c.getIdCompra() == null) throw new Exception("Compra inválida.");
        dao.update(conn, c);
    }

    public void eliminarCompra(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) throw new Exception("ID inválido.");
        dao.delete(conn, id);
    }

    public Compra obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<Compra> listarTodas() throws Exception {
        return dao.findAll(conn);
    }
}
