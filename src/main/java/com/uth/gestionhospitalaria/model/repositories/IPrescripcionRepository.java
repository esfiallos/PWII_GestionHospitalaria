package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.Prescripcion;
import java.util.List;

public interface IPrescripcionRepository {
    List<Prescripcion> listarTodos();
    Prescripcion buscarPorId(int id);
    boolean crear(Prescripcion prescripcion);
    boolean actualizar(Prescripcion prescripcion);
    boolean eliminar(int id);


    // Podría ser: List<Prescripcion> listarPorHistorial(int idHistorial);
}