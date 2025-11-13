package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.CitaMedica;

import java.util.List;

public interface ICitaRepository {
    List<CitaMedica> listarTodas();

    CitaMedica buscarPorId(int id);

    boolean crear(CitaMedica cita);

    boolean actualizar(CitaMedica cita);

    boolean eliminar(int id);

    List<CitaMedica> listarPorPaciente(int id);

    // Metodos Pendientes
    // List<CitaMedica> listarPorDoctor(int idDoctor);
}
