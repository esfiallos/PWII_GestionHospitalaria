package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.Clients.CitaClient;
import com.uth.gestionhospitalaria.model.repositories.ICitaRepository;

import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import java.util.List;

public class CitasRepositoryImpl implements ICitaRepository {

    private CitaClient citaClient;

    public CitasRepositoryImpl() {
        this.citaClient = RetrofitClient.getCitaApiClient();
    }
    @Override
    public List<CitaMedica> listarTodas() {
        try {
            Response<OrdsResponse<CitaMedica>> response = citaClient.listarTodas().execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public CitaMedica buscarPorId(int id) {
        try {
            Response<OrdsResponse<CitaMedica>> response = citaClient.buscarPorId(id).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean crear(CitaMedica cita) {
        try {
            Response<Void> response = citaClient.crear(cita).execute();
            return response.isSuccessful() || response.code() == 201; // 201 Created
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(CitaMedica cita) {
        try {
            Response<Void> response = citaClient.actualizar(cita.getId_cita(), cita).execute();
            return response.isSuccessful(); // 200 OK
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Response<Void> response = citaClient.eliminar(id).execute();
            return response.isSuccessful() || response.code() == 204; // 204 No Content
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
