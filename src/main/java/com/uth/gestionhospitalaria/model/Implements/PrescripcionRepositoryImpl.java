package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Prescripcion;
import com.uth.gestionhospitalaria.model.Clients.PrescripcionClient;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.repositories.IPrescripcionRepository;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class PrescripcionRepositoryImpl implements IPrescripcionRepository {

    private PrescripcionClient prescripcionClient;

    public PrescripcionRepositoryImpl() {
        this.prescripcionClient = RetrofitClient.getPrescripcionClient();
    }

    @Override
    public List<Prescripcion> listarTodos() {
        try {
            Response<OrdsResponse<Prescripcion>> response = prescripcionClient.listarTodos().execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) { e.printStackTrace(); }
        return Collections.emptyList();
    }

    @Override
    public Prescripcion buscarPorId(int id) {
        try {
            Response<OrdsResponse<Prescripcion>> response = prescripcionClient.buscarPorId(id).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().get(0);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean crear(Prescripcion prescripcion) {
        try {
            Response<Void> response = prescripcionClient.crear(prescripcion).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(Prescripcion prescripcion) {
        try {
            Response<Void> response = prescripcionClient.actualizar(prescripcion.getId_prescripcion(), prescripcion).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Response<Void> response = prescripcionClient.eliminar(id).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
}