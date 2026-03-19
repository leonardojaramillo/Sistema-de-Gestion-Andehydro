package main.java.com.andehydro.model;

import java.sql.Date;

public class Factura {

    private String idFactura;
    private Date fechaEmision;
    private double energiaConsumida;
    private double montoTotal;
    private String estadoPago;
    private String idContrato;

    public Factura() {}

    public Factura(String idFactura, Date fechaEmision, double energiaConsumida,
                   double montoTotal, String estadoPago, String idContrato) {

        this.idFactura = idFactura;
        this.fechaEmision = fechaEmision;
        this.energiaConsumida = energiaConsumida;
        this.montoTotal = montoTotal;
        this.estadoPago = estadoPago;
        this.idContrato = idContrato;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getEnergiaConsumida() {
        return energiaConsumida;
    }

    public void setEnergiaConsumida(double energiaConsumida) {
        this.energiaConsumida = energiaConsumida;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(String idContrato) {
        this.idContrato = idContrato;
    }
}
