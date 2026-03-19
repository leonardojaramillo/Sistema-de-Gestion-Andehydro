package main.java.com.andehydro.service;

import main.java.com.andehydro.dao.FacturaDAO;
import main.java.com.andehydro.model.Factura;

import java.sql.Connection;
import java.util.List;

public class FacturaService {

    private final Connection conn;
    private final FacturaDAO dao;

    public FacturaService(Connection conn) {
        this.conn = conn;
        this.dao = new FacturaDAO();
    }

    public void crearFactura(Factura f) throws Exception {
        if (f == null) throw new IllegalArgumentException("Factura nula");
        if (f.getIdFactura() == null || f.getIdFactura().isEmpty())
            throw new Exception("ID obligatorio.");
        if (f.getMontoTotal() <= 0)
            throw new Exception("Monto debe ser mayor a 0.");
        if (f.getEstadoPago() == null || f.getEstadoPago().isEmpty())
            f.setEstadoPago("Pendiente");

        dao.insert(conn, f);
    }

    public void actualizarFactura(Factura f) throws Exception {
        if (f == null || f.getIdFactura() == null)
            throw new Exception("Factura inválida.");
        dao.update(conn, f);
    }

    public void eliminarFactura(String id) throws Exception {
        if (id == null || id.isEmpty())
            throw new Exception("ID inválido.");
        dao.delete(conn, id);
    }

    public Factura obtenerPorId(String id) throws Exception {
        return dao.findById(conn, id);
    }

    public List<Factura> listarTodas() throws Exception {
        return dao.findAll(conn);
    }
}
