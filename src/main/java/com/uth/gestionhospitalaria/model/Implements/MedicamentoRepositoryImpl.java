package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.Clients.MedicamentoClient;
import com.uth.gestionhospitalaria.model.repositories.IMedicamentoRepository;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MedicamentoRepositoryImpl implements IMedicamentoRepository {

    private final MedicamentoClient medicamentoClient;

    public MedicamentoRepositoryImpl() {
        this.medicamentoClient = RetrofitClient.getMedicamentoClient();
    }

    @Override
    public List<Medicamento> listarTodos() {
        try {
            Response<OrdsResponse<Medicamento>> response = medicamentoClient.listarTodos().execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Medicamento buscarPorId(int id) {
        try {
            Response<OrdsResponse<Medicamento>> response = medicamentoClient.buscarPorId(id).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean crear(Medicamento medicamento) {
        try {
            Response<Void> response = medicamentoClient.crear(medicamento).execute();
            return response.isSuccessful() || response.code() == 201;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Medicamento medicamento) {
        try {
            Response<Void> response = medicamentoClient.actualizar(medicamento.getId_medicamento(), medicamento).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Response<Void> response = medicamentoClient.eliminar(id).execute();
            return response.isSuccessful() || response.code() == 204;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
