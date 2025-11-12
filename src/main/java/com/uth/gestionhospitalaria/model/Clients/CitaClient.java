package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface CitaClient {
    // GET .../GH/citas/
    @GET("citas/")
    Call<OrdsResponse<CitaMedica>> listarTodas();

    // GET .../GH/citas/{id}
    @GET("citas/{id}")
     Call<OrdsResponse<CitaMedica>> buscarPorId(@Path("id") int id);

    // POST .../GH/citas/
    @POST("citas/")
    Call<Void> crear(@Body CitaMedica cita);

    // PUT .../GH/citas/{id}
    @PUT("citas/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body CitaMedica cita);

    // DELETE .../GH/citas/{id}
    @DELETE("citas/{id}")
    Call<Void> eliminar(@Path("id") int id);
}
