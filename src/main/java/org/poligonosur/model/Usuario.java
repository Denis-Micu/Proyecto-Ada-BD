package org.poligonosur.model;

import java.time.LocalDateTime;

// Usuario de la aplicación. Corresponde a una fila de la tabla usuario.
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los campos, para crear el usuario al leerlo de la base de datos
    public Usuario(int idUsuario, String nombre, String email, String password, LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // El email es el que se usa para iniciar sesión
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Devuelve el nombre, para mostrarlo en la interfaz
    @Override
    public String toString() {
        return nombre;
    }
}
