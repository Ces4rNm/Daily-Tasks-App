package com.proyect.dailytasks;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
public interface PostService {
    @GET("/daily_tasks/Consultas/mostrarTareas.php")
    Call<List<Post>> getPost();
}
