package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IFacturaInteractor;
import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.model.Implements.FacturaRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.IFacturaRepository;
import java.util.List;

public class FacturaInteractorImpl implements IFacturaInteractor {

    private final IFacturaRepository facturaRepository;

    public FacturaInteractorImpl() {
        this.facturaRepository = new FacturaRepositoryImpl();
    }

    @Override
    public List<Factura> consultarFacturas() {
        return this.facturaRepository.listarTodos();
    }

    @Override
    public Factura consultarFacturaPorId(int id) {
        return this.facturaRepository.buscarPorId(id);
    }

    @Override
    public boolean generarFactura(Factura factura) {
        if (factura.getId_cita_fk() == 0 || factura.getId_paciente_fk() == 0 || factura.getMonto_total() <= 0) {
            System.err.println("Error de negocio: Cita, Paciente y Monto > 0 son obligatorios.");
            return false;
        }
        factura.setEstado_pago("PENDIENTE"); // Regla de negocio
        return this.facturaRepository.crear(factura);
    }

    @Override
    public boolean marcarComoPagada(int idFactura) {
        Factura factura = this.facturaRepository.buscarPorId(idFactura);
        if (factura == null) {
            System.err.println("Error de negocio: No se encontró la factura.");
            return false;
        }
        factura.setEstado_pago("PAGADA"); // Regla de negocio
        return this.facturaRepository.actualizar(factura);
    }
}