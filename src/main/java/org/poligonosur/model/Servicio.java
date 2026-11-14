package org.poligonosur.model;

// Servicio al que se puede suscribir el usuario (Netflix, Spotify...). Corresponde a una fila de la tabla servicio.
public class Servicio {

    private int idServicio;
    // Es Integer y no int porque en la tabla puede ser NULL (servicio sin categoría)
    private Integer idCategoria;
    private String nombre;
    private String url;
    private String logo;

    // Constructor vacío
    public Servicio() {
    }

    // Constructor con todos los campos, para crear el servicio al leerlo de la base de datos
    public Servicio(int idServicio, Integer idCategoria, String nombre, String url, String logo) {
        this.idServicio = idServicio;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.url = url;
        this.logo = logo;
    }

    // Getters y setters

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    // Devuelve el nombre, para que se vea bien en los desplegables
    @Override
    public String toString() {
        return nombre;
    }
}
