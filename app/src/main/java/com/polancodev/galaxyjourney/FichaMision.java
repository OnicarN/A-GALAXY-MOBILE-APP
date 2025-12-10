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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class FichaMision extends AppCompatActivity {
    Button btnWeb,btnAmigo,btnNotify,btnParche;
    ImageView img ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha_mision);
        cargarComponentes();
        MisionModel laMision = recogerMision();
        cargarImagen(laMision);


        //En esta parte es la que busco en el internete
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String url = laMision.getWiki();
                Intent intentWeb = new Intent(Intent.ACTION_VIEW);
                intentWeb.setData(Uri.parse(url));
                startActivity(intentWeb);
            }
        });

        //Esta es la parte en la que le envió a mis amigos la invitación
        btnAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = "¡Mira este lanzamiento\n" +
                        laMision.getNombreMision()+" el día 20/10!";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mensaje);

                // Esto crea un selector para que el usuario elija la app
                startActivity(Intent.createChooser(intent, "Compartir lanzamiento vía:"));
            }
        });
        btnParche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarImagen(laMision);
            }
        });

    }

    public void cargarComponentes (){
        btnWeb = findViewById(R.id.webOficial);
        btnAmigo = findViewById(R.id.invitarAmigo);
        btnNotify = findViewById(R.id.notificarme);
        btnParche = findViewById(R.id.ObtenerParche);
        img = findViewById(R.id.imagenFichaMision);
    }
    public MisionModel recogerMision (){
        Intent intent = getIntent();
        MisionModel mision = intent.getParcelableExtra("mision");
        return mision;
    }
    public void cargarImagen(MisionModel mision){
        Glide.with(this)
                .load(mision.getUrlImagen())
                .into(img);
    }

    public void descargarImagen (MisionModel mision){
        // 1. La URL del archivo
        String url = mision.getUrlImagen();
        // 2. Crear la solicitud
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 4. Configurar dónde se guarda (Carpeta pública "Downloads")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Foto.png");
        // 5. Obtener el servicio del sistema y lanzar la petición
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
        Toast.makeText(this, "Se esta descargando", Toast.LENGTH_SHORT).show();
    }
}
