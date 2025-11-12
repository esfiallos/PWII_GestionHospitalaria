package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.model.Clients.FacturaClient;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.repositories.IFacturaRepository;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FacturaRepositoryImpl implements IFacturaRepository {

    private FacturaClient facturaClient;

    public FacturaRepositoryImpl() {
        this.facturaClient = RetrofitClient.getFacturaClient();
    }

    @Override
    public List<Factura> listarTodos() {
        try {
            Response<OrdsResponse<Factura>> response = facturaClient.listarTodos().execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) { e.printStackTrace(); }
        return Collections.emptyList();
    }

    @Override
    public Factura buscarPorId(int id) {
        try {
            Response<OrdsResponse<Factura>> response = facturaClient.buscarPorId(id).execute();
            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().get(0);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean crear(Factura factura) {
        try {
            Response<Void> response = facturaClient.crear(factura).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(Factura factura) {
        try {
            Response<Void> response = facturaClient.actualizar(factura.getId_factura(), factura).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Response<Void> response = facturaClient.eliminar(id).execute();
            return response.isSuccessful();
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
}