package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface MedicamentoClient {

    // GET .../GH/medicamentos/
    @GET("medicamentos/")
    Call<OrdsResponse<Medicamento>> listarTodos();

    // GET .../GH/medicamentos/{id}
    @GET("medicamentos/{id}")
    Call<OrdsResponse<Medicamento>> buscarPorId(@Path("id") int id);

    // POST .../GH/medicamentos/
    @POST("medicamentos/")
    Call<Void> crear(@Body Medicamento medicamento);

    // PUT .../GH/medicamentos/{id}
    @PUT("medicamentos/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body Medicamento medicamento);

    // DELETE .../GH/medicamentos/{id}
    @DELETE("medicamentos/{id}")
    Call<Void> eliminar(@Path("id") int id);
}