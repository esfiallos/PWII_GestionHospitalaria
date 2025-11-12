package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.Paciente;
import java.util.List;


public interface IPacienteInteractor {

    List<Paciente> consultarPacientes();

    Paciente consultarPacientePorId(int id);

    boolean registrarPaciente(Paciente paciente);

    boolean actualizarPaciente(Paciente paciente);

    boolean eliminarPaciente(int id);
}