package main.java.com.andehydro.model;

public class Cliente {

    private String idCliente;
    private String nombre;
    private String razonSocial;
    private String tipoCliente;
    private String ruc;
    private String direccion;
    private String telefono;
    private String idCentral; // FK

    public Cliente() {}

    public Cliente(String idCliente, String nombre, String razonSocial, String tipoCliente,
                   String ruc, String direccion, String telefono, String idCentral) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.tipoCliente = tipoCliente;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.idCentral = idCentral;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdCentral() {
        return idCentral;
    }

    public void setIdCentral(String idCentral) {
        this.idCentral = idCentral;
    }

    @Override
    public String toString() {
        return nombre + " (" + razonSocial + ")";
    }
}

