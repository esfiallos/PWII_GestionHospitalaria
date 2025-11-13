package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.HistorialClinico;
import java.util.List;

public interface IHistorialInteractor {

    List<HistorialClinico> consultarHistoriales();

    HistorialClinico consultarHistorialPorId(int id);

    // HistorialClinico consultarHistorialPorCita(int idCita);

    boolean registrarEnHistorial(HistorialClinico historial);

    boolean actualizarHistorial(HistorialClinico historial);
}