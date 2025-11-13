package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IHistorialInteractor;
import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.model.Implements.HistorialRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.IHistorialRepository;
import java.util.List;

public class HistorialInteractorImpl implements IHistorialInteractor {

    private final IHistorialRepository historialRepository;

    public HistorialInteractorImpl() {
        this.historialRepository = new HistorialRepositoryImpl();
    }

    @Override
    public List<HistorialClinico> consultarHistoriales() {
        return this.historialRepository.listarTodos();
    }

    @Override
    public HistorialClinico consultarHistorialPorId(int id) {
        return this.historialRepository.buscarPorId(id);
    }

    @Override
    public boolean registrarEnHistorial(HistorialClinico historial) {
        if (historial.getId_cita_fk() == 0 || historial.getDiagnostico() == null) {
            System.err.println("Error de negocio: La cita y el diagnóstico son obligatorios.");
            return false;
        }
        return this.historialRepository.crear(historial);
    }

    @Override
    public boolean actualizarHistorial(HistorialClinico historial) {
        if (historial.getId_historial() == 0) {
            System.err.println("Error de negocio: No se puede actualizar un historial sin ID.");
            return false;
        }
        return this.historialRepository.actualizar(historial);
    }
}