package org.poligonosur.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Pago de una suscripción (historial de cobros). Corresponde a una fila de la tabla pago.
public class Pago {

    private int idPago;
    private int idSuscripcion;
    private LocalDate fechaPago;
    // BigDecimal porque en la tabla es DECIMAL(10,2)
    private BigDecimal importe;
    // PAGADO o PENDIENTE
    private String estado;

    // Constructor vacío
    public Pago() {
    }

    // Constructor con todos los campos, para crear el pago al leerlo de la base de datos
    public Pago(int idPago, int idSuscripcion, LocalDate fechaPago, BigDecimal importe, String estado) {
        this.idPago = idPago;
        this.idSuscripcion = idSuscripcion;
        this.fechaPago = fechaPago;
        this.importe = importe;
        this.estado = estado;
    }

    // Getters y setters

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Devuelve la fecha y el importe, por ejemplo "2026-10-17 - 12.99 €", para el historial
    @Override
    public String toString() {
        return fechaPago + " - " + importe + " €";
    }
}
