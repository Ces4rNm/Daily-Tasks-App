package com.proyect.dailytasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayAdapter arrayAdapter;
    ArrayList<String> tareas = new ArrayList<>();
    Vibrator vibe;
    Calendar mClndr;
    DatePickerDialog dpd;
    Boolean filter = true;

    public void agregar_tarea(View v){
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

        final EditText etFinicio = findViewById(R.id.etFinicio);
        etFinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mClndr = Calendar.getInstance();
                int day = mClndr.get(Calendar.DAY_OF_MONTH);
                int month = mClndr.get(Calendar.MONTH);
                int year = mClndr.get(Calendar.YEAR);

                dpd = new DatePickerDialog(MainActivity.this, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        etFinicio.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        final EditText etFfinal = findViewById(R.id.etFfinal);
        etFfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mClndr = Calendar.getInstance();
                int day = mClndr.get(Calendar.DAY_OF_MONTH);
                int month = mClndr.get(Calendar.MONTH);
                int year = mClndr.get(Calendar.YEAR);

                dpd = new DatePickerDialog(MainActivity.this, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        etFfinal.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        final ImageView img = (ImageView) findViewById(R.id.imageViewFilter);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!etFinicio.getText().toString().equals("") && !etFfinal.getText().toString().equals("")) {
                    if (filter) {
                        img.setImageResource(R.drawable.ic_baseline_delete_sweep_24);
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.0.2/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FilterRangeDate filterRangeDate = retrofit.create(FilterRangeDate.class);
                        Call<List<Post>> call = filterRangeDate.getPost(etFinicio.getText().toString(), etFfinal.getText().toString());
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
                    } else {
                        img.setImageResource(R.drawable.ic_baseline_filter_list_24);
                        etFinicio.setText("");
                        etFfinal.setText("");
                        getPosts();
                    }
                    filter = !filter;
                    vibe.vibrate(100);
                } else {
                    Toast.makeText(MainActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
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