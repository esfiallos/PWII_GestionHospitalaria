package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.Paciente;

import java.util.List;

public interface IPacienteRepository {
    List<Paciente> listarTodos();

    Paciente buscarPorId(int id);

    boolean crear(Paciente paciente);

    boolean actualizar(Paciente paciente);

    boolean eliminar(int id);
}
