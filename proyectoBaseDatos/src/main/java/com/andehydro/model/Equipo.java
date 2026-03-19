package main.java.com.andehydro.model;

import java.sql.Date;

public class Equipo {

    private String idEquipo;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private Date fechaInstalacion;
    private String estado;
    private String idCentral;

    public Equipo() {}

    public Equipo(String idEquipo, String tipoEquipo, String marca, String modelo,
                  Date fechaInstalacion, String estado, String idCentral) {
        this.idEquipo = idEquipo;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.fechaInstalacion = fechaInstalacion;
        this.estado = estado;
        this.idCentral = idCentral;
    }

    // Getters & Setters

    public String getIdEquipo() { return idEquipo; }
    public void setIdEquipo(String idEquipo) { this.idEquipo = idEquipo; }

    public String getTipoEquipo() { return tipoEquipo; }
    public void setTipoEquipo(String tipoEquipo) { this.tipoEquipo = tipoEquipo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Date getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(Date fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getIdCentral() { return idCentral; }
    public void setIdCentral(String idCentral) { this.idCentral = idCentral; }
}
