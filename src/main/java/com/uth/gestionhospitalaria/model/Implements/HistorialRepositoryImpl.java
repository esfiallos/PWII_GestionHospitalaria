package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.model.Clients.HistorialClient;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.repositories.IHistorialRepository;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HistorialRepositoryImpl implements IHistorialRepository {

    private final HistorialClient historialClient;

    public HistorialRepositoryImpl() {
        this.historialClient = RetrofitClient.getHistorialClient();
    }

    @Override
    public List<HistorialClinico> listarTodos() {
        try {
            Response<OrdsResponse<HistorialClinico>> response = historialClient.listarTodos().execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) { e.printStackTrace(); }
        return Collections.emptyList();
    }

    @Override
    public HistorialClinico buscarPorId(int id) {
        try {
            Response<OrdsResponse<HistorialClinico>> response = historialClient.buscarPorId(id).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().get(0);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean crear(HistorialClinico historial) {
        try {
            Response<Void> response = historialClient.crear(historial).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(HistorialClinico historial) {
        try {
            Response<Void> response = historialClient.actualizar(historial.getId_historial(), historial).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Response<Void> response = historialClient.eliminar(id).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
}