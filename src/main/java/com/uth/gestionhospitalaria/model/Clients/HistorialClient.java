package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface HistorialClient {
    @GET("historiales/")
    Call<OrdsResponse<HistorialClinico>> listarTodos();

    @GET("historiales/{id}")
    Call<OrdsResponse<HistorialClinico>> buscarPorId(@Path("id") int id);

    @POST("historiales/")
    Call<Void> crear(@Body HistorialClinico historial);

    @PUT("historiales/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body HistorialClinico historial);

    @DELETE("historiales/{id}")
    Call<Void> eliminar(@Path("id") int id);
}