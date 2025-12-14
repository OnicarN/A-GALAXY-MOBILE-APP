package com.polancodev.galaxyjourney;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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

        // ⬅️ PERMISO PARA ANDROID 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    100
            );
        }

        arrayMisiones = new ArrayList<>();
        adapter = new MisionAdapter(this, arrayMisiones);
        recyclerView = findViewById(R.id.recylerMisiones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        descargaMisiones();


    }




    private void descargaMisiones() {

        ApiMissionService api = MiRetrofit.getApiService();

        Call<List<MisionModel>> llamada = api.obtenerMisiones();

        llamada.enqueue(new Callback<List<MisionModel>>() {
            @Override
            public void onResponse(Call<List<MisionModel>> call,
                                   Response<List<MisionModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayMisiones.clear();
                    arrayMisiones.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CentroDeControl.this,
                            "Error al cargar misiones",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MisionModel>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CentroDeControl.this,
                        "Fallo de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
