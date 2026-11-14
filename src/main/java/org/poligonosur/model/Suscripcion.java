package org.poligonosur.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Suscripción de un usuario a un servicio. Corresponde a una fila de la tabla suscripcion.
public class Suscripcion {

    private int idSuscripcion;
    private int idUsuario;
    private int idServicio;
    // BigDecimal porque en la tabla es DECIMAL(10,2) y así no se pierden céntimos
    private BigDecimal importe;
    // MENSUAL, TRIMESTRAL, SEMESTRAL o ANUAL
    private String periodicidad;
    private LocalDate fechaInicio;
    private LocalDate proximoPago;
    // ACTIVA, PAUSADA o CANCELADA
    private String estado;
    private int diasAviso;
    private String notas;

    // No es una columna de la tabla: se rellena con un JOIN a servicio para mostrar el nombre en los listados
    private String nombreServicio;

    // Constructor vacío
    public Suscripcion() {
    }

    // Constructor con todas las columnas, para crear la suscripción al leerla de la base de datos
    public Suscripcion(int idSuscripcion, int idUsuario, int idServicio, BigDecimal importe, String periodicidad,
                       LocalDate fechaInicio, LocalDate proximoPago, String estado, int diasAviso, String notas) {
        this.idSuscripcion = idSuscripcion;
        this.idUsuario = idUsuario;
        this.idServicio = idServicio;
        this.importe = importe;
        this.periodicidad = periodicidad;
        this.fechaInicio = fechaInicio;
        this.proximoPago = proximoPago;
        this.estado = estado;
        this.diasAviso = diasAviso;
        this.notas = notas;
    }

    // Getters y setters

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getProximoPago() {
        return proximoPago;
    }

    public void setProximoPago(LocalDate proximoPago) {
        this.proximoPago = proximoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getDiasAviso() {
        return diasAviso;
    }

    public void setDiasAviso(int diasAviso) {
        this.diasAviso = diasAviso;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    // Devuelve el nombre del servicio, para mostrarlo en la interfaz
    @Override
    public String toString() {
        return nombreServicio;
    }
}
