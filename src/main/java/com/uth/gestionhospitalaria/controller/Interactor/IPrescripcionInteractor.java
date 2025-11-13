package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.Prescripcion;
import java.util.List;

public interface IPrescripcionInteractor {

    List<Prescripcion> consultarPrescripciones();

    // List<Prescripcion> consultarPrescripcionesPorHistorial(int idHistorial);

    boolean registrarPrescripcion(Prescripcion prescripcion);

    boolean eliminarPrescripcion(int id);
}