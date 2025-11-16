package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.HistorialInteractorImpl;
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
import java.util.List;

@Named("historialBean")
@ViewScoped
public class HistorialBean implements Serializable {

    private HistorialInteractorImpl historialInteractor;
    private PrescripcionInteractorImpl prescripcionInteractor;
    private HistorialViewModel historialViewModel;

    @PostConstruct
    public void init() {
        this.historialInteractor = new HistorialInteractorImpl();
        this.prescripcionInteractor = new PrescripcionInteractorImpl();
        this.historialViewModel = new HistorialViewModel();
        cargarHistoriales();
    }

    public void cargarHistoriales(){
        historialViewModel.setEstaCargando(true);
        try {
            List<HistorialClinico> historial = historialInteractor.consultarHistoriales();
            historialViewModel.setListaHistoriales(historial);
            historialViewModel.setMensajeError(null);
        }catch (Exception ex){
            historialViewModel.setMensajeError(ex.getMessage());
            historialViewModel.setListaHistoriales(List.of());
        }finally {
            historialViewModel.setEstaCargando(false);
        }
    }

    public void nuevoHistorial(){
        historialViewModel.setHistorialSeleccionado(new HistorialClinico());
        historialViewModel.setPrescripcionesDelHistorial(List.of());
        historialViewModel.setPrescripcionNueva(new Prescripcion());
    }

    public void seleccionarHistorial(HistorialClinico historial){
        historialViewModel.setHistorialSeleccionado(historial);
        cargarPrescripciones(historial.getId_historial());
    }

    public void cargarPrescripciones(int idHistorial){
        List<Prescripcion> todas = prescripcionInteractor.consultarPrescripciones();
        List<Prescripcion> filtrar = todas.stream().filter(p -> p.getId_historial_fk() == idHistorial).toList();
        historialViewModel.setPrescripcionesDelHistorial(filtrar);
    }

    public void guardarHistorial(){
        HistorialClinico historial = historialViewModel.getHistorialSeleccionado();
        boolean resultado;
        String mensaje;

        try {
            if (historial.getId_historial() == 0) {
                resultado = historialInteractor.registrarEnHistorial(historial);
                mensaje = resultado ? "Historial registrado" : "Error al registrar";
            } else {
                resultado = historialInteractor.actualizarHistorial(historial);
                mensaje = resultado ? "Historial actualizado" : "Error al actualizar";
            }

            if (resultado) {
                cargarHistoriales();
                nuevoHistorial();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public void agregarPrescripcion() {
        Prescripcion nueva = historialViewModel.getPrescripcionNueva();
        nueva.setId_historial_fk(historialViewModel.getHistorialSeleccionado().getId_historial());

        boolean resultado = prescripcionInteractor.registrarPrescripcion(nueva);
        if (resultado) {
            cargarPrescripciones(nueva.getId_historial_fk());
            historialViewModel.setPrescripcionNueva(new Prescripcion());
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Prescripción agregada");
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo agregar la prescripción");
        }
    }

    public void eliminarPrescripcion(Prescripcion prescripcion) {
        boolean resultado = prescripcionInteractor.eliminarPrescripcion(prescripcion.getId_prescripcion());
        if (resultado) {
            cargarPrescripciones(prescripcion.getId_historial_fk());
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Prescripción eliminada");
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la prescripción");
        }
    }

    public HistorialViewModel getHistorialViewModel() {
        return historialViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
