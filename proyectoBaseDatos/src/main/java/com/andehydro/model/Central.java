package main.java.com.andehydro.model;

public class Central {

    private String idCentral;
    private String nombre;
    private String ubicacion;
    private double capacidadMW;
    private int añoInicio;
    private String tipoPlanta;

    public Central() {}

    public Central(String idCentral, String nombre, String ubicacion,
                   double capacidadMW, int añoInicio, String tipoPlanta) {
        this.idCentral = idCentral;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMW = capacidadMW;
        this.añoInicio = añoInicio;
        this.tipoPlanta = tipoPlanta;
    }

    public String getIdCentral() { return idCentral; }
    public void setIdCentral(String idCentral) { this.idCentral = idCentral; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getCapacidadMW() { return capacidadMW; }
    public void setCapacidadMW(double capacidadMW) { this.capacidadMW = capacidadMW; }

    public int getAñoInicio() { return añoInicio; }
    public void setAñoInicio(int añoInicio) { this.añoInicio = añoInicio; }

    public String getTipoPlanta() { return tipoPlanta; }
    public void setTipoPlanta(String tipoPlanta) { this.tipoPlanta = tipoPlanta; }
}
