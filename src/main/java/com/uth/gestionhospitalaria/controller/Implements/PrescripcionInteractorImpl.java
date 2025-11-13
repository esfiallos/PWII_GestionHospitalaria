package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IPrescripcionInteractor;
import com.uth.gestionhospitalaria.data.Prescripcion;
import com.uth.gestionhospitalaria.model.Implements.PrescripcionRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.IPrescripcionRepository;
import java.util.List;

public class PrescripcionInteractorImpl implements IPrescripcionInteractor {

    private final IPrescripcionRepository prescripcionRepository;

    public PrescripcionInteractorImpl() {
        this.prescripcionRepository = new PrescripcionRepositoryImpl();
    }

    @Override
    public List<Prescripcion> consultarPrescripciones() {
        return this.prescripcionRepository.listarTodos();
    }

    @Override
    public boolean registrarPrescripcion(Prescripcion prescripcion) {
        if (prescripcion.getId_historial_fk() == 0 || prescripcion.getId_medicamento_fk() == 0 || prescripcion.getCantidad() <= 0) {
            System.err.println("Error de negocio: Historial, Medicamento y Cantidad > 0 son obligatorios.");
            return false;
        }
        return this.prescripcionRepository.crear(prescripcion);
    }

    @Override
    public boolean eliminarPrescripcion(int id) {
        return this.prescripcionRepository.eliminar(id);
    }
}