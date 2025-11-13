package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.controller.Implements.CitaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.CitaViewModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors; // Para filtrar doctores

@Named("citaBean")
@ViewScoped
public class CitaBean implements Serializable {

    // ¡Múltiples dependencias!
    private ICitaInteractor citaInteractor;
    private IPacienteInteractor pacienteInteractor;
    private IUsuarioInteractor usuarioInteractor;

    private CitaViewModel viewModel;

    @PostConstruct
    public void init() {
        this.citaInteractor = new CitaInteractorImpl();
        this.pacienteInteractor = new PacienteInteractorImpl();
        this.usuarioInteractor = new UsuarioInteractorImpl();
        this.viewModel = new CitaViewModel();

        // Carga inicial
        this.cargarCitas();
        this.cargarListasDesplegables();
    }

    public CitaViewModel getViewModel() {
        return viewModel;
    }

    // --- Métodos de Carga ---
    public void cargarCitas() {
        viewModel.setEstaCargando(true);
        try {
            viewModel.setListaCitas(citaInteractor.consultarCitas());
            viewModel.setMensajeError(null);
        } catch (Exception e) {
            viewModel.setMensajeError("Error al cargar citas: " + e.getMessage());
            e.printStackTrace();
        }
        viewModel.setEstaCargando(false);
    }

    public void cargarListasDesplegables() {
        try {
            // Cargar pacientes
            viewModel.setListaPacientesDropdown(pacienteInteractor.consultarPacientes());

            // Cargar solo usuarios que son 'DOCTOR'
            List<Usuario> doctores = usuarioInteractor.consultarUsuarios().stream()
                    .filter(u -> "DOCTOR".equals(u.getRol()) && "ACTIVO".equals(u.getEstado()))
                    .collect(Collectors.toList());
            viewModel.setListaDoctoresDropdown(doctores);

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las listas para los formularios");
            e.printStackTrace();
        }
    }

    // --- Acciones de Botones ---
    public void nuevo() {
        viewModel.setCitaSeleccionada(new CitaMedica());
    }

    public void guardar() {
        CitaMedica cita = viewModel.getCitaSeleccionada();
        boolean resultado = false;
        String mensaje = "";
        try {
            if (cita.getId_cita() == 0) {
                resultado = citaInteractor.agendarCita(cita);
                mensaje = "Cita agendada";
            } else {
                resultado = citaInteractor.actualizarCita(cita);
                mensaje = "Cita actualizada";
            }

            if (resultado) {
                cargarCitas();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la cita (verifique las reglas de negocio)");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    // Llama a la regla de negocio "cancelarCita"
    public void cancelar() {
        try {
            boolean resultado = citaInteractor.cancelarCita(viewModel.getCitaSeleccionada().getId_cita());
            if(resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cita marcada como 'CANCELADA'");
                cargarCitas();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cancelar la cita");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    // Helper
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
