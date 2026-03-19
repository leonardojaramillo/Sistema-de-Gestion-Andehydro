package main.java.com.andehydro.model;

import java.sql.Date;

public class Empleado {

    private String idEmpleado;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String area;
    private Date fechaIngreso;
    private String idJefe;
    private String idCentral;

    public Empleado() {}

    public Empleado(String idEmpleado, String nombres, String apellidos, String cargo,
                    String area, Date fechaIngreso, String idJefe, String idCentral) {
        this.idEmpleado = idEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.area = area;
        this.fechaIngreso = fechaIngreso;
        this.idJefe = idJefe;
        this.idCentral = idCentral;
    }

    // Getters y Setters

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getIdJefe() { return idJefe; }
    public void setIdJefe(String idJefe) { this.idJefe = idJefe; }

    public String getIdCentral() { return idCentral; }
    public void setIdCentral(String idCentral) { this.idCentral = idCentral; }
}
