
package com.polancodev.galaxyjourney;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FichaMision extends AppCompatActivity {
    Button btnWeb, btnAmigo, btnNotify, btnParche;
    TextView txt;
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha_mision);

        cargarComponentes();
        MisionModel laMision = recogerMision();
        txt.setText(laMision.getDescripcion());
        cargarImagen(laMision);


        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = laMision.getWiki();
                Intent intentWeb = new Intent(Intent.ACTION_VIEW);
                intentWeb.setData(Uri.parse(url));
                startActivity(intentWeb);
            }
        });


        btnAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = "¡Mira este lanzamiento\n" +
                        laMision.getNombreMision() + " el día 20/10!";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                startActivity(Intent.createChooser(intent, "Compartir lanzamiento vía:"));
            }
        });


        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suscribirseConVolley(laMision);
            }
        });


        btnParche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarImagen(laMision);
            }
        });
    }

    public void cargarComponentes() {
        btnWeb = findViewById(R.id.webOficial);
        btnAmigo = findViewById(R.id.invitarAmigo);
        btnNotify = findViewById(R.id.notificarme);
        btnParche = findViewById(R.id.ObtenerParche);
        img = findViewById(R.id.imagenFichaMision);
        txt = findViewById(R.id.descripcion);
    }

    public MisionModel recogerMision() {
        Intent intent = getIntent();
        MisionModel mision = intent.getParcelableExtra("mision");
        return mision;
    }

    public void cargarImagen(MisionModel mision) {
        Glide.with(this)
                .load(mision.getUrlImagen())
                .into(img);
    }


    private void suscribirseConVolley(MisionModel mision) {
        String url = "http://pabloglezs.es/subscribe.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("id_mision", String.valueOf(mision.getId())); // usa el id numérico
        params.put("token_usuario", "pabloglezbm@gmail.com");

        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    try {
                        String status = response.getString("status");
                        String mensaje = response.getString("message");
                        if ("subscribed".equalsIgnoreCase(status)) {
                            Toast.makeText(FichaMision.this, mensaje, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FichaMision.this, "" + mensaje, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(FichaMision.this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FichaMision.this, "Error al suscribirse", Toast.LENGTH_SHORT).show()
        );

        queue.add(postRequest);
    }



    public void descargarImagen(MisionModel mision) {
        String url = mision.getUrlImagen();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));


        request.setTitle("Parche de Misión");
        request.setDescription("Descargando " + mision.getNombreMision());
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        );


        String nombreArchivo = mision.getNombreMision().replaceAll("\\s+", "_") + ".png";
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                nombreArchivo
        );


        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            long downloadId = manager.enqueue(request);


            getSharedPreferences("downloads", MODE_PRIVATE)
                    .edit()
                    .putLong("download_id", downloadId)
                    .apply();

            Toast.makeText(this, "Descargando parche...", Toast.LENGTH_SHORT).show();
        }
    }
}