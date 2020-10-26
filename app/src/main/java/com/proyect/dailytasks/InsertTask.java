package com.proyect.dailytasks;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InsertTask {
    @POST("/daily_tasks/Consultas/crearTarea.php")
    @FormUrlEncoded
    Call<PostResult> insertTask(@Field("descripcion") String descripcion, @Field("fecha") String fecha);
}