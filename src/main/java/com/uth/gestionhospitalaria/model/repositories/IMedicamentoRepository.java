package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.Medicamento;
import java.util.List;

public interface IMedicamentoRepository {
    List<Medicamento> listarTodos();
    Medicamento buscarPorId(int id);
    boolean crear(Medicamento medicamento);
    boolean actualizar(Medicamento medicamento);
    boolean eliminar(int id);
}