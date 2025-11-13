package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.IFacturaInteractor;
import com.uth.gestionhospitalaria.controller.Implements.FacturaInteractorImpl;
import com.uth.gestionhospitalaria.view.FacturaViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("facturaBean")
@ViewScoped
public class FacturaBean implements Serializable {

    private IFacturaInteractor facturaInteractor;
    private FacturaViewModel viewModel;

    @PostConstruct
    public void init() {
        this.facturaInteractor = new FacturaInteractorImpl();
        this.viewModel = new FacturaViewModel();
        this.cargarFacturas();
    }

    public FacturaViewModel getViewModel() {
        return viewModel;
    }

    public void cargarFacturas() {
        viewModel.setEstaCargando(true);
        try {
            viewModel.setListaFacturas(facturaInteractor.consultarFacturas());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar facturas");
        }
        viewModel.setEstaCargando(false);
    }

    // Este Bean es más simple, solo tiene lógica para "Pagar"

    public void marcarComoPagada() {
        try {
            boolean resultado = facturaInteractor.marcarComoPagada(viewModel.getFacturaSeleccionada().getId_factura());
            if(resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Factura marcada como PAGADA");
                cargarFacturas();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la factura");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}