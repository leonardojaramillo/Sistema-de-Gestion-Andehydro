package main.java.com.andehydro.model;
import java.util.Date;

public class Compra {

    private String idCompra;
    private Date fecha;
    private String descripcion;
    private double monto;
    private String tipoMoneda;
    private String idProveedor;
    private String idProyecto;

    public Compra() {}

    public Compra(String idCompra, Date fecha, String descripcion,
                  double monto, String tipoMoneda,
                  String idProveedor, String idProyecto) {

        this.idCompra = idCompra;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoMoneda = tipoMoneda;
        this.idProveedor = idProveedor;
        this.idProyecto = idProyecto;
    }

    public String getIdCompra() { return idCompra; }
    public void setIdCompra(String idCompra) { this.idCompra = idCompra; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getTipoMoneda() { return tipoMoneda; }
    public void setTipoMoneda(String tipoMoneda) { this.tipoMoneda = tipoMoneda; }

    public String getIdProveedor() { return idProveedor; }
    public void setIdProveedor(String idProveedor) { this.idProveedor = idProveedor; }

    public String getIdProyecto() { return idProyecto; }
    public void setIdProyecto(String idProyecto) { this.idProyecto = idProyecto; }
}
