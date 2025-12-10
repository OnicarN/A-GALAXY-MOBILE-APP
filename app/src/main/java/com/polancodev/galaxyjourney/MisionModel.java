package com.polancodev.galaxyjourney;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
public class MisionModel implements Parcelable {

    @SerializedName("mision")
    String nombreMision;

    @SerializedName("agencia")
    String agencia;

    @SerializedName("rocket_img")
    String imagenMision;

    @SerializedName("patch_img")
    String urlImagen;

    @SerializedName("wiki_url")
    String wiki;

    public MisionModel(String nombreMision, String agencia, String imagenMision) {
        this.nombreMision = nombreMision;
        this.agencia = agencia;
        this.imagenMision = imagenMision;
    }

    protected MisionModel(Parcel in) {
        nombreMision = in.readString();
        agencia = in.readString();
        imagenMision = in.readString();
        urlImagen = in.readString();
        wiki = in.readString();
    }

    public static final Creator<MisionModel> CREATOR = new Creator<MisionModel>() {
        @Override
        public MisionModel createFromParcel(Parcel in) {
            return new MisionModel(in);
        }

        @Override
        public MisionModel[] newArray(int size) {
            return new MisionModel[size];
        }
    };

    public String getNombreMision() {
        return nombreMision;
    }

    public void setNombreMision(String nombreMision) {
        this.nombreMision = nombreMision;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getImagenMision() {
        return imagenMision;
    }

    public void setImagenMision(String imagenMision) {
        this.imagenMision = imagenMision;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nombreMision);
        parcel.writeString(agencia);
        parcel.writeString(imagenMision);
        parcel.writeString(urlImagen);
        parcel.writeString(wiki);
    }
}