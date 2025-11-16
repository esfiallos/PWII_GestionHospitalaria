package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.CitaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.view.CitaViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("citaBean")
@ViewScoped
public class CitaBean implements Serializable {
    private ICitaInteractor citaInteractor;
    private CitaViewModel citaViewModel;

    @PostConstruct
    public void init() {
        this.citaInteractor = new CitaInteractorImpl();
        this.citaViewModel = new CitaViewModel();
        this.cargarCita();
    }

    public void cargarCita() {
        citaViewModel.setEstaCargando(true);
        try{
            List<CitaMedica> cita = citaInteractor.consultarCitas();
            citaViewModel.setListaCitas(cita);
            citaViewModel.setMensajeError(null);
        }catch(Exception ex){
            citaViewModel.setMensajeError("Error" + ex.getMessage());
            citaViewModel.setListaCitas(List.of());
        }
        citaViewModel.setEstaCargando(false);
    }



    public void nuevo(){
        citaViewModel.setCitaSeleccionada(new CitaMedica());
    }

    public void guardar(){
        CitaMedica cita = new CitaMedica();
        boolean resultado;
        String mensaje;

        try{
            if(cita.getId_cita() == 0) {
                resultado = this.citaInteractor.actualizarCita(cita);
                mensaje = resultado ? "Cita creada con éxito" : "Error ";
            }else{
                resultado = citaInteractor.actualizarCita(cita);
                mensaje = resultado ? "Cita actualizada con éxito" : "Error";
            }

            if(resultado){
                cargarCita();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }

        }catch(Exception ex){
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public void eliminar(){
        try {
            boolean resultado = citaInteractor.cancelarCita(citaViewModel.getCitaSeleccionada().getId_cita());
            if(resultado){
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cita eliminada");
                cargarCita();
                nuevo();
            }
        }catch(Exception ex) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public CitaViewModel getCitaViewModel() {
        return citaViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
