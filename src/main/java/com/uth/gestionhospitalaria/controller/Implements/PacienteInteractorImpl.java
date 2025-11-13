package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.model.Implements.CitasRepositoryImpl;
import com.uth.gestionhospitalaria.model.Implements.PacienteRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.ICitaRepository;
import com.uth.gestionhospitalaria.model.repositories.IPacienteRepository;

import java.util.List;


public class PacienteInteractorImpl implements IPacienteInteractor {

    private final IPacienteRepository pacienteRepository;
    ICitaRepository citaRepository;

    public PacienteInteractorImpl() {
        this.pacienteRepository = new PacienteRepositoryImpl();
        this.citaRepository = new CitasRepositoryImpl();
    }

    @Override
    public List<Paciente> consultarPacientes() {
        // Simplemente llama al repositorio
        return this.pacienteRepository.listarTodos();
    }

    @Override
    public Paciente consultarPacientePorId(int id) {
        // Llama al repositorio
        return this.pacienteRepository.buscarPorId(id);
    }

    @Override
    public boolean registrarPaciente(Paciente paciente) {

        //  validación 1:
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            System.err.println("Error : El DNI no puede estar vacío.");
            return false;
        }

        // validación 2:
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.err.println("Error : El nombre no puede estar vacío.");
            return false;
        }


        // Si todas las validaciones pasan, se llama al repositorio
        return this.pacienteRepository.crear(paciente);
    }

    @Override
    public boolean actualizarPaciente(Paciente paciente) {

        if (paciente.getId_paciente() == 0) {
            System.err.println("Error: No se puede actualizar un paciente sin ID.");
            return false;
        }

        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            System.err.println("Error: El DNI no puede estar vacío.");
            return false;
        }

        return this.pacienteRepository.actualizar(paciente);
    }

    @Override
    public boolean eliminarPaciente(int id) {
         List<CitaMedica> citas = citaRepository.listarPorPaciente(id);
         if (!citas.isEmpty()) {
           System.err.println("Error: No se puede eliminar un paciente con citas.");
            return false;
         }
        // Si pasa las validaciones, se llama al repositorio
        return this.pacienteRepository.eliminar(id);
    }
}