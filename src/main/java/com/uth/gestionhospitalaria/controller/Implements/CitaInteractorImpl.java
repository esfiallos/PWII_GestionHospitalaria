package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.model.Implements.CitasRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.ICitaRepository;
import java.util.List;

public class CitaInteractorImpl implements ICitaInteractor {

    private ICitaRepository citaRepository;

    public CitaInteractorImpl() {
        this.citaRepository = new CitasRepositoryImpl();
    }

    @Override
    public List<CitaMedica> consultarCitas() {
        return this.citaRepository.listarTodas();
    }

    @Override
    public CitaMedica consultarCitaPorId(int id) {
        return this.citaRepository.buscarPorId(id);
    }


    @Override
    public boolean agendarCita(CitaMedica cita) {
        // 1. La validación principal sigue igual
        if (cita.getId_paciente_fk() == 0 || cita.getId_doctor_fk() == 0 || cita.getFecha_hora_cita() == null) {
            System.err.println("Error de negocio: Paciente, Doctor y Fecha son obligatorios.");
            return false;
        }

        // 2. La validación de fecha ahora es MÁS FÁCIL.
        // Compara un objeto Date contra un nuevo objeto Date (ahora).
        if (cita.getFecha_hora_cita().before(new java.util.Date())) {
            System.err.println("Error de negocio: No se puede agendar una cita en el pasado.");
            return false;
        }

        cita.setEstado_cita("AGENDADA");

        return this.citaRepository.crear(cita);
    }

    @Override
    public boolean actualizarCita(CitaMedica cita) {
        if (cita.getId_cita() == 0) {
            System.err.println("Error de negocio: No se puede actualizar una cita sin ID.");
            return false;
        }
        return this.citaRepository.actualizar(cita);
    }

    @Override
    public boolean cancelarCita(int id) {
        // 1. Buscar la cita
        CitaMedica cita = this.citaRepository.buscarPorId(id);
        if (cita == null) {
            System.err.println("Error: No se encontró la cita a cancelar.");
            return false;
        }

        // 2. Cambio de Estado
        cita.setEstado_cita("CANCELADA");

        // 3. Actualizar
        return this.citaRepository.actualizar(cita);
    }
}