package main.java.com.andehydro.model;

public class Proveedor {

    private String idProveedor;
    private String nombre;
    private String razonSocial;
    private String ruc;
    private String tipoServicio;
    private String pais;

    public Proveedor() {}

    public Proveedor(String idProveedor, String nombre, String razonSocial,
                     String ruc, String tipoServicio, String pais) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.tipoServicio = tipoServicio;
        this.pais = pais;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
