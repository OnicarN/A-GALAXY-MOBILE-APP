package com.polancodev.galaxyjourney;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CentroDeControl extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MisionModel> arrayMisiones;
    MisionAdapter adapter;
    List<MisionModel> listaMisiones = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arrayMisiones = new ArrayList<>();
        adapter = new MisionAdapter(this, arrayMisiones);
        recyclerView = findViewById(R.id.recylerMisiones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        descargaMisiones();
    }

    private void descargaMisiones() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pabloglezs.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiMissionService api = retrofit.create(ApiMissionService.class);

        Call<List<MisionModel>> llamada = api.obtenerMisiones();

        llamada.enqueue(new Callback<List<MisionModel>>() {
            @Override
            public void onResponse(Call<List<MisionModel>> call, Response<List<MisionModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayMisiones.clear();
                    arrayMisiones.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<MisionModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}



