package com.proyect.dailytasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteResult {
    @SerializedName("Mensaje")
    @Expose
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "DeleteResult{" +
                "Mensaje='" + mensaje + '\'' +
                '}';
    }
}
