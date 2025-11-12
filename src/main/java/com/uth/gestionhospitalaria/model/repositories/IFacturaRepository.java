package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.Factura;
import java.util.List;

public interface IFacturaRepository {
    List<Factura> listarTodos();
    Factura buscarPorId(int id);
    boolean crear(Factura factura);
    boolean actualizar(Factura factura);
    boolean eliminar(int id);
    // Puede ser: List<Factura> listarPorPaciente(int idPaciente);
}