package com.proyect.dailytasks;

public class Post {
    private String codigoTarea;
    private String descripcion;
    private String fecha;

    public Post(String codigoTarea, String descripcion, String fecha) {
        this.codigoTarea = codigoTarea;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getCodigoTarea() {
        return codigoTarea;
    }

    public void setCodigoTarea(String codigoTarea) {
        this.codigoTarea = codigoTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
