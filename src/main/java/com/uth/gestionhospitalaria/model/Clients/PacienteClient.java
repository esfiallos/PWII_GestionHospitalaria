package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Paciente;
import retrofit2.Call;
import retrofit2.http.*;

public interface PacienteClient {

        // Llama a: GET .../GH/pacientes/
        @GET("pacientes/")
        Call<OrdsResponse<Paciente>> listarTodos();

        // Llama a: GET .../GH/pacientes/{id}
        @GET("pacientes/{id}")
        Call<OrdsResponse<Paciente>> buscarPorId(@Path("id") int id);

        // Llama a: POST .../GH/pacientes/
        @POST("pacientes/")
        Call<Void> crear(@Body Paciente paciente);

        // Llama a: PUT .../GH/pacientes/{id}
        @PUT("pacientes/{id}")
        Call<Void> actualizar(@Path("id") int id, @Body Paciente paciente);

        // Llama a: DELETE .../GH/pacientes/{id}
        @DELETE("pacientes/{id}")
        Call<Void> eliminar(@Path("id") int id);
}
