package org.poligonosur.model;

import java.math.BigDecimal;

// Resumen de lo que gasta un usuario. No es una tabla: guarda lo que devuelve el procedimiento sp_resumen_gasto.
public class ResumenGasto {

    private BigDecimal gastoMensual;
    private BigDecimal gastoAnual;
    private int numActivas;

    // Constructor vacío
    public ResumenGasto() {
    }

    // Constructor con todos los campos, para crearlo con los parámetros OUT del procedimiento
    public ResumenGasto(BigDecimal gastoMensual, BigDecimal gastoAnual, int numActivas) {
        this.gastoMensual = gastoMensual;
        this.gastoAnual = gastoAnual;
        this.numActivas = numActivas;
    }

    // Getters y setters

    public BigDecimal getGastoMensual() {
        return gastoMensual;
    }

    public void setGastoMensual(BigDecimal gastoMensual) {
        this.gastoMensual = gastoMensual;
    }

    public BigDecimal getGastoAnual() {
        return gastoAnual;
    }

    public void setGastoAnual(BigDecimal gastoAnual) {
        this.gastoAnual = gastoAnual;
    }

    public int getNumActivas() {
        return numActivas;
    }

    public void setNumActivas(int numActivas) {
        this.numActivas = numActivas;
    }
}
