package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.model.Clients.PacienteClient;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.repositories.IPacienteRepository;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class PacienteRepositoryImpl implements IPacienteRepository {


        // La interfaz de Retrofit
        private PacienteClient pacienteClient;

        public PacienteRepositoryImpl() {
            this.pacienteClient = RetrofitClient.getPacienteApiClient();
        }

        @Override
        public List<Paciente> listarTodos() {
            try {
                Call<OrdsResponse<Paciente>> call = pacienteClient.listarTodos();
                Response<OrdsResponse<Paciente>> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    return response.body().getItems(); // Devuelve la lista limpia
                }
            } catch (IOException e) {
                e.printStackTrace(); // Manejar el error
            }
            return Collections.emptyList();
        }

        @Override
        public Paciente buscarPorId(int id) {
            try {
                Call<OrdsResponse<Paciente>> call = pacienteClient.buscarPorId(id);
                Response<OrdsResponse<Paciente>> response = call.execute();

                if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                    return response.body().getItems().getFirst();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public boolean crear(Paciente paciente) {
            try {
                Response<Void> response = pacienteClient.crear(paciente).execute();
                // 201 Created es el código de éxito para un POST
                return response.isSuccessful() || response.code() == 201;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean actualizar(Paciente paciente) {
            try {
                Response<Void> response = pacienteClient.actualizar(paciente.getId_paciente(), paciente).execute();
                // 200 OK es el código de éxito para un PUT
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean eliminar(int id) {
            try {
                Response<Void> response = pacienteClient.eliminar(id).execute();
                // 204 No Content es el código de éxito para un DELETE
                return response.isSuccessful() || response.code() == 204;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
}
