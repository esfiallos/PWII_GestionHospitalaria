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
        if (cita.getId_paciente_fk() == 0 || cita.getId_doctor_fk() == 0 || cita.getFecha_hora_cita() == null) {
            System.err.println("Error de negocio: Paciente, Doctor y Fecha son obligatorios.");
            return false;
        }
        // Aquí podrías validar que la fecha no sea en el pasado
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
        // En lugar de un borrado físico (DELETE),
        // lo ideal sería un borrado lógico (actualizar el estado a 'CANCELADA')
        // Pero para seguir el patrón del CRUD, llamamos a eliminar:
        return this.citaRepository.eliminar(id);
    }
}