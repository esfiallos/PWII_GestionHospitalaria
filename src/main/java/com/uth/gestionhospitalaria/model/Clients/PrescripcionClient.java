package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Prescripcion;
import retrofit2.Call;
import retrofit2.http.*;

public interface PrescripcionClient {
    @GET("prescripciones/")
    Call<OrdsResponse<Prescripcion>> listarTodos();

    @GET("prescripciones/{id}")
    Call<OrdsResponse<Prescripcion>> buscarPorId(@Path("id") int id);

    @POST("prescripciones/")
    Call<Void> crear(@Body Prescripcion prescripcion);

    @PUT("prescripciones/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body Prescripcion prescripcion);

    @DELETE("prescripciones/{id}")
    Call<Void> eliminar(@Path("id") int id);
}