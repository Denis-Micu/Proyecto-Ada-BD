package org.poligonosur.model;

// Categoría de un servicio (Streaming, Música...). Corresponde a una fila de la tabla categoria.
public class Categoria {

    private int idCategoria;
    private String nombre;
    private String color;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con todos los campos, para crear la categoría al leerla de la base de datos
    public Categoria(int idCategoria, String nombre, String color) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.color = color;
    }

    // Getters y setters

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // El color va en formato hexadecimal
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Devuelve el nombre, para que se vea bien en los desplegables
    @Override
    public String toString() {
        return nombre;
    }
}
