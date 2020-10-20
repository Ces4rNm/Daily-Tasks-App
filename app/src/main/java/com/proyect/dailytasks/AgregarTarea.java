

package com.proyect.dailytasks;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class AgregarTarea extends AppCompatActivity {

    Calendar mClndr;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        final EditText etFecha = findViewById(R.id.etFecha);

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mClndr = Calendar.getInstance();
                int day = mClndr.get(Calendar.DAY_OF_MONTH);
                int month = mClndr.get(Calendar.MONTH);
                int year = mClndr.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AgregarTarea.this, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        etFecha.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }

    public void guardar_tarea(View v) throws JSONException {

        EditText etDescripcion = findViewById(R.id.etDescripcion);
        EditText etFecha = findViewById(R.id.etFecha);

        if(!etDescripcion.getText().toString().equals("") && !etFecha.getText().toString().equals("")) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.2/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            InsertTask InsertTask = retrofit.create(InsertTask.class);

            // prepare call in Retrofit 2.0
            try {
                JSONObject paramObject = new JSONObject();
                paramObject.put("descripcion", etDescripcion.getText().toString());
                paramObject.put("fecha", etFecha.getText().toString());

                Call<PostResult> response = InsertTask.insertTask(etDescripcion.getText().toString(), etFecha.getText().toString());
                response.enqueue(new Callback<PostResult>() {
                    @Override
                    public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                        if(response.isSuccessful()) {
                            Log.i("MSG", "Response de la API." + response.body().toString());
                            insertTask();
                        } else {
                            Log.e("notSuccessful", String.valueOf(response));
                        }
                    }
                    @Override
                    public void onFailure(Call<PostResult> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTask() {
        Toast.makeText(this, "Tarea agregada exitosamente", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }
}