package com.proyect.dailytasks;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FilterRangeDate {
    @POST("/daily_tasks/Consultas/mostrarTareasFechas.php")
    @FormUrlEncoded
    Call<List<Post>> getPost(@Field("fechaInicial") String fechaInicial, @Field("fechaFinal") String fechaFinal);
}
