package com.polancodev.galaxyjourney;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiMissionService {
    @GET("lanzamientos.json")
    Call<List<MisionModel>> obtenerMisiones();

    @GET("lanzamientos.json")
    Call<MisionModel> infoIndividual();
}
