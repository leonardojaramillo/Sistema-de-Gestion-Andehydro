package main.java.com.andehydro.model;

import java.sql.Date;

public class ProyectoRSE {

    private String idProyecto;
    private String nombre;
    private String descripcion;
    private double inversion;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private String idCentral;

    public ProyectoRSE() {}

    public ProyectoRSE(String idProyecto, String nombre, String descripcion, double inversion,
                       Date fechaInicio, Date fechaFin, String estado, String idCentral) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inversion = inversion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.idCentral = idCentral;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getInversion() {
        return inversion;
    }

    public void setInversion(double inversion) {
        this.inversion = inversion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdCentral() {
        return idCentral;
    }

    public void setIdCentral(String idCentral) {
        this.idCentral = idCentral;
    }
}
