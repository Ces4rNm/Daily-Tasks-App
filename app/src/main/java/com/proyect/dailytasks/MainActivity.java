package com.proyect.dailytasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayAdapter arrayAdapter;
    ArrayList<String> tareas = new ArrayList<>();
    Vibrator vibe;

    public void agregar_tarea(View v){
        vibe.vibrate(100);
        Intent i = new Intent(this, AgregarTarea.class );
        startActivity(i);
    }

    public void filtrar_tarea(View v){
        vibe.vibrate(100);
        Intent i = new Intent(this, AgregarTarea.class );
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibe = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        arrayAdapter = new ArrayAdapter(this, R.layout.rowlayout, R.id.firstLine, tareas);
        list = (ListView) findViewById(R.id.list);
        getPosts();
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                view.findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAlert(position);
                    }
                });
            }
        });
    }

    private void getPosts() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService postService = retrofit.create(PostService.class);
        Call<List<Post>> call = postService.getPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                tareas.clear();
                arrayAdapter.notifyDataSetChanged();
                for(Post post : response.body()) {
                    tareas.add(post.getCodigoTarea()+" - "+post.getDescripcion()+"  ["+post.getFecha()+"]");
                }
                arrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Error", "onFailure:"+t.getMessage());
            }
        });
    }

    private void getAlert(int position) {
        final String id = tareas.get(position).split(" -")[0];
        vibe.vibrate(100);
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme)
        //set icon
        .setIcon(android.R.drawable.ic_dialog_alert)
        //set title
        .setTitle("Borrar")
        //set message
        .setMessage("Deseas borrar la tarea id:"+id+"?")
        //set positive button
        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                Log.e("Item", "id:"+id+" delete");
                if(!id.equals("")) {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.0.2/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    DeleteTask deleteTask = retrofit.create(DeleteTask.class);

                    Call<DeleteResult> response = deleteTask.deleteTask(id);
                    response.enqueue(new Callback<DeleteResult>() {
                        @Override
                        public void onResponse(Call<DeleteResult> call, Response<DeleteResult> response) {
                            if(response.isSuccessful()) {
                                Log.i("MSG", "Response de la API." + response.body().toString());
                                getPosts();
                            } else {
                                Log.e("notSuccessful", String.valueOf(response));
                            }
                        }
                        @Override
                        public void onFailure(Call<DeleteResult> call, Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });
                } else {
                    Log.e("Error", "id no valido");
                }
            }
        })
        //set negative button
        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //set what should happen when negative button is clicked
                Toast.makeText(getApplicationContext(),"Acción cancelada",Toast.LENGTH_LONG).show();
            }
        })
        .show();
    }
}