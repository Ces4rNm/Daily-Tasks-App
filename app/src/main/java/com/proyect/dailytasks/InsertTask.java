package com.proyect.dailytasks;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InsertTask {
    String API_ROUTE = "/daily_tasks/Consultas/crearTarea.php";
    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<PostResult> insertTask(@Field("descripcion") String descripcion, @Field("fecha") String fecha);
}