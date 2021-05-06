package Mapa;

import java.io.Serializable;

public class Establecimiento implements Serializable {
    int imagen;
    String nombre;
    String tipo;
    String direccion;
    String sitioWeb;
    String horario;
    boolean servicio;
    String telefono;

    public Establecimiento(int imagen, String nombre, String tipo, String direccion, String sitioWeb, String horario, boolean servicio, String telefono) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
        this.sitioWeb = sitioWeb;
        this.horario = horario;
        this.servicio = servicio;
        this.telefono = telefono;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isServicio() {
        return servicio;
    }

    public void setServicio(boolean servicio) {
        this.servicio = servicio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}