package com.proyect.dailytasks;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
public interface PostService {
    String API_ROUTE = "/daily_tasks/Consultas/mostrarTareas.php";
    @GET(API_ROUTE)
    Call<List<Post>> getPost();
}
