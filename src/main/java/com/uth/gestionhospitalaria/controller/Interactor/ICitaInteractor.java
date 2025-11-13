package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.CitaMedica;
import java.util.List;

public interface ICitaInteractor {

    List<CitaMedica> consultarCitas();

    CitaMedica consultarCitaPorId(int id);

    boolean agendarCita(CitaMedica cita);

    boolean actualizarCita(CitaMedica cita);

    boolean cancelarCita(int id);
}
