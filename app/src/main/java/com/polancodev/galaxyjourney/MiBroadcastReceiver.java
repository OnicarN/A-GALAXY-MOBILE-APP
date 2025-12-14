package com.polancodev.galaxyjourney;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {

            long idDescarga =
                    intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            long idGuardado =
                    context.getSharedPreferences("downloads", Context.MODE_PRIVATE)
                            .getLong("download_id", -1);


            if (idDescarga == idGuardado) {

                DownloadManager manager =
                        (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(idDescarga);

                Cursor cursor = manager.query(query);

                if (cursor.moveToFirst()) {
                    String uriDescarga =
                            cursor.getString(cursor.getColumnIndexOrThrow(
                                    DownloadManager.COLUMN_LOCAL_URI));

                    mostrarNotificacion(context, Uri.parse(uriDescarga));
                }

                cursor.close();
            }
        }
    }

    private void mostrarNotificacion(Context context, Uri uriImagen) {

        String channelId = "canal_descargas";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    channelId,
                    "Descargas Galaxy",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(canal);
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uriImagen, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.star_big_on)
                        .setContentTitle("Parche de misión guardado")
                        .setContentText("Pulsa para ver la imagen")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(100, builder.build());
    }
}
