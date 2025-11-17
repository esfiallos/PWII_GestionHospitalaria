package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.model.Implements.CitasRepositoryImpl;
import com.uth.gestionhospitalaria.model.Implements.FacturaRepositoryImpl;
import com.uth.gestionhospitalaria.model.Implements.HistorialRepositoryImpl;
import com.uth.gestionhospitalaria.model.Implements.PacienteRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.ICitaRepository;
import com.uth.gestionhospitalaria.model.repositories.IFacturaRepository;
import com.uth.gestionhospitalaria.model.repositories.IHistorialRepository;
import com.uth.gestionhospitalaria.model.repositories.IPacienteRepository;

import java.util.List;


public class PacienteInteractorImpl implements IPacienteInteractor {

    private final IPacienteRepository pacienteRepository;

    private final ICitaRepository citaRepository;
    private final IFacturaRepository facturaRepository;
    private final IHistorialRepository historialRepository;

    public PacienteInteractorImpl() {
        this.pacienteRepository = new PacienteRepositoryImpl();

        // --- CAMBIO: Instanciar los nuevos repositorios ---
        this.citaRepository = new CitasRepositoryImpl();
        this.facturaRepository = new FacturaRepositoryImpl();
        this.historialRepository = new HistorialRepositoryImpl();
    }

    @Override
    public List<Paciente> consultarPacientes() {

        return this.pacienteRepository.listarTodos();
    }

    @Override
    public Paciente consultarPacientePorId(int id) {

        return this.pacienteRepository.buscarPorId(id);
    }

    @Override
    public boolean registrarPaciente(Paciente paciente) {


        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            System.err.println("Error : El DNI no puede estar vacío.");
            return false;
        }


        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.err.println("Error : El nombre no puede estar vacío.");
            return false;
        }


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


        List<HistorialClinico> historiales = historialRepository.listarTodos().stream()
                .filter(h -> h.getId_paciente_fk() == id)
                .toList();
        if (!historiales.isEmpty()) {
            System.err.println("Error: No se puede eliminar un paciente con historiales clínicos.");
            return false;
        }

        List<Factura> facturas = facturaRepository.listarTodos().stream()
                .filter(f -> f.getId_paciente_fk() == id)
                .toList();
        if (!facturas.isEmpty()) {
            System.err.println("Error: No se puede eliminar un paciente con facturas.");
            return false;
        }

        return this.pacienteRepository.eliminar(id);
    }
}