package main.java.com.andehydro.model;

import java.util.Date;

public class ContratoEnergia {

    private String idContrato;
    private Date fechaInicio;
    private Date fechaFin;
    private double tarifaKw;
    private String tipoMoneda;
    private String idCliente;

    public ContratoEnergia() {}

    public ContratoEnergia(String idContrato, Date fechaInicio, Date fechaFin,
                           double tarifaKw, String tipoMoneda, String idCliente) {

        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tarifaKw = tarifaKw;
        this.tipoMoneda = tipoMoneda;
        this.idCliente = idCliente;
    }

    public String getIdContrato() { return idContrato; }
    public void setIdContrato(String idContrato) { this.idContrato = idContrato; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public double getTarifaKw() { return tarifaKw; }
    public void setTarifaKw(double tarifaKw) { this.tarifaKw = tarifaKw; }

    public String getTipoMoneda() { return tipoMoneda; }
    public void setTipoMoneda(String tipoMoneda) { this.tipoMoneda = tipoMoneda; }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }
}
