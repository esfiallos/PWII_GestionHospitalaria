package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.model.Implements.MedicamentoRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.IMedicamentoRepository;
import java.util.List;

public class MedicamentoInteractorImpl implements IMedicamentoInteractor {

    private final IMedicamentoRepository medicamentoRepository;

    public MedicamentoInteractorImpl() {
        this.medicamentoRepository = new MedicamentoRepositoryImpl();
    }

    @Override
    public List<Medicamento> consultarMedicamentos() {
        return this.medicamentoRepository.listarTodos();
    }

    @Override
    public Medicamento consultarMedicamentoPorId(int id) {
        return this.medicamentoRepository.buscarPorId(id);
    }

    @Override
    public boolean registrarMedicamento(Medicamento medicamento) {
        if (medicamento.getNombre_comercial() == null || medicamento.getNombre_comercial().trim().isEmpty()) {
            System.err.println("Error de negocio: El nombre es obligatorio.");
            return false;
        }
        return this.medicamentoRepository.crear(medicamento);
    }

    @Override
    public boolean actualizarMedicamento(Medicamento medicamento) {
        if (medicamento.getId_medicamento() == 0) {
            System.err.println("Error de negocio: No se puede actualizar un medicamento sin ID.");
            return false;
        }
        return this.medicamentoRepository.actualizar(medicamento);
    }

    @Override
    public boolean eliminarMedicamento(int id) {
        return this.medicamentoRepository.eliminar(id);
    }
}