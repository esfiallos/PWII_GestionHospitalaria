package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.HistorialClinico;
import java.util.List;

public interface IHistorialRepository {
    List<HistorialClinico> listarTodos();
    HistorialClinico buscarPorId(int id);
    boolean crear(HistorialClinico historial);
    boolean actualizar(HistorialClinico historial);
    boolean eliminar(int id);
}