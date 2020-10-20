package com.proyect.dailytasks;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeleteTask {
    String API_ROUTE = "/daily_tasks/Consultas/borrarTarea.php";
    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<DeleteResult> deleteTask(@Field("codigoTarea") String codigoTarea);
}
