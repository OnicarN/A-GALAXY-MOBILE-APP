package com.polancodev.galaxyjourney;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MisionAdapter extends RecyclerView.Adapter<MisionAdapter.MisionViewHolder> {

    Context context;
    ArrayList<MisionModel> arrayMisiones;

    public MisionAdapter(Context context, ArrayList<MisionModel> arrayMisiones) {
        this.context = context;
        this.arrayMisiones = arrayMisiones;
    }

    @NonNull
    @Override
    public MisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mission_item,parent,false);
        return new MisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisionViewHolder holder, int position) {
        MisionModel misionModel = arrayMisiones.get(position);
        holder.nombre.setText(misionModel.getNombreMision());
        holder.agencia.setText(misionModel.getAgencia());

        Glide.with(context)
                .load(misionModel.getImagenMision())

                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FichaMision.class);
            intent.putExtra("mision", misionModel);
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return arrayMisiones.size();
    }

    public  class MisionViewHolder extends RecyclerView.ViewHolder{
     TextView nombre;
     TextView agencia;
     ImageView img;
     public MisionViewHolder(@NonNull View itemView) {
         super(itemView);
         nombre = itemView.findViewById(R.id.rc_name);
         agencia = itemView.findViewById(R.id.rc_agency);
         img =itemView.findViewById(R.id.rc_image);
     }
 }
}
