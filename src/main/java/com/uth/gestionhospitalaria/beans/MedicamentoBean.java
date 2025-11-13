package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.controller.Implements.MedicamentoInteractorImpl;
import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.view.MedicamentoViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("medicamentoBean")
@ViewScoped
public class MedicamentoBean implements Serializable {

    private IMedicamentoInteractor medicamentoInteractor;
    private MedicamentoViewModel viewModel;

    @PostConstruct
    public void init() {
        this.medicamentoInteractor = new MedicamentoInteractorImpl();
        this.viewModel = new MedicamentoViewModel();
        this.cargarMedicamentos();
    }

    public MedicamentoViewModel getViewModel() {
        return viewModel;
    }

    public void cargarMedicamentos() {
        viewModel.setEstaCargando(true);
        try {
            viewModel.setListaMedicamentos(medicamentoInteractor.consultarMedicamentos());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar medicamentos");
        }
        viewModel.setEstaCargando(false);
    }

    public void nuevo() {
        viewModel.setMedicamentoSeleccionado(new Medicamento());
    }

    public void guardar() {
        Medicamento med = viewModel.getMedicamentoSeleccionado();
        boolean resultado;
        String mensaje;
        try {
            if (med.getId_medicamento() == 0) {
                resultado = medicamentoInteractor.registrarMedicamento(med);
                mensaje = "Medicamento registrado";
            } else {
                resultado = medicamentoInteractor.actualizarMedicamento(med);
                mensaje = "Medicamento actualizado";
            }
            if (resultado) {
                cargarMedicamentos();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public void eliminar() {
        try {
            boolean resultado = medicamentoInteractor.eliminarMedicamento(viewModel.getMedicamentoSeleccionado().getId_medicamento());
            if (resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Medicamento eliminado");
                cargarMedicamentos();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}