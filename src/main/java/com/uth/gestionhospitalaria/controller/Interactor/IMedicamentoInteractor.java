package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.Medicamento;
import java.util.List;

public interface IMedicamentoInteractor {

    List<Medicamento> consultarMedicamentos();

    Medicamento consultarMedicamentoPorId(int id);

    boolean registrarMedicamento(Medicamento medicamento);

    boolean actualizarMedicamento(Medicamento medicamento);

    boolean eliminarMedicamento(int id);
}