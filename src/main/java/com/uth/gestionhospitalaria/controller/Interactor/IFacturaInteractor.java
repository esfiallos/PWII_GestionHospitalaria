package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.Factura;
import java.util.List;

public interface IFacturaInteractor {

    List<Factura> consultarFacturas();

    Factura consultarFacturaPorId(int id);

    // List<Factura> consultarFacturasPorPaciente(int idPaciente);

    boolean generarFactura(Factura factura);

    boolean marcarComoPagada(int idFactura);

    boolean actualizarFactura(Factura factura);
}