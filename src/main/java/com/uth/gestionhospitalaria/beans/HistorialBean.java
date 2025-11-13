package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.IHistorialInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPrescripcionInteractor;
import com.uth.gestionhospitalaria.controller.Implements.HistorialInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.MedicamentoInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PrescripcionInteractorImpl;
import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.Prescripcion;
import com.uth.gestionhospitalaria.view.HistorialViewModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("historialBean")
@ViewScoped
public class HistorialBean implements Serializable {

    // Múltiples interactors
    private IHistorialInteractor historialInteractor;
    private IPrescripcionInteractor prescripcionInteractor;
    private IMedicamentoInteractor medicamentoInteractor;

    private HistorialViewModel viewModel;

    @PostConstruct
    public void init() {
        this.historialInteractor = new HistorialInteractorImpl();
        this.prescripcionInteractor = new PrescripcionInteractorImpl();
        this.medicamentoInteractor = new MedicamentoInteractorImpl();
        this.viewModel = new HistorialViewModel();

        this.cargarHistoriales();
        this.cargarCatalogoMedicamentos();
    }

    public HistorialViewModel getViewModel() {
        return viewModel;
    }

    public void cargarHistoriales() {
        viewModel.setEstaCargando(true);
        try {
            viewModel.setListaHistoriales(historialInteractor.consultarHistoriales());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar historiales");
        }
        viewModel.setEstaCargando(false);
    }

    public void cargarCatalogoMedicamentos() {
        try {
            viewModel.setCatalogoMedicamentos(medicamentoInteractor.consultarMedicamentos());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el catálogo de medicamentos");
        }
    }

    // Acción para cuando se selecciona un historial de la tabla
    public void onHistorialSeleccionado() {
        try {
            // Cargar las prescripciones de este historial
            // (Esta es una regla de negocio que falta,
            // por ahora, solo cargaremos todas)
            viewModel.setPrescripcionesDelHistorial(prescripcionInteractor.consultarPrescripciones());

            // Filtrar las prescripciones por el ID del historial
            // (Asumiendo que el Interactor/Repo no tiene un método "buscarPorHistorial")
            int idHistorial = viewModel.getHistorialSeleccionado().getId_historial();
            viewModel.getPrescripcionesDelHistorial().removeIf(p -> p.getId_historial_fk() != idHistorial);

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las prescripciones");
        }
    }

    public void guardarPrescripcion() {
        try {
            // Asignar el ID del historial seleccionado a la nueva prescripción
            int idHistorial = viewModel.getHistorialSeleccionado().getId_historial();
            viewModel.getPrescripcionNueva().setId_historial_fk(idHistorial);

            boolean resultado = prescripcionInteractor.registrarPrescripcion(viewModel.getPrescripcionNueva());
            if (resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Prescripción añadida");
                onHistorialSeleccionado(); // Recargar la lista
                viewModel.setPrescripcionNueva(new Prescripcion()); // Limpiar
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo añadir la prescripción");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    // (Aquí faltarían guardarHistorial, etc., pero sigue el mismo patrón)

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}