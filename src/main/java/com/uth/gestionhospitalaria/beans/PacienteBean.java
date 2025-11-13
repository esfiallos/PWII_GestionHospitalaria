package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.view.PacienteViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("pacienteBean") //  #{pacienteBean})
@ViewScoped
public class PacienteBean implements Serializable {

    // 1. Dependencias (La lógica y el estado)
    private IPacienteInteractor pacienteInteractor;
    private PacienteViewModel viewModel;

    // 2. Inicialización
    @PostConstruct
    public void init() {

        this.pacienteInteractor = new PacienteInteractorImpl();
        this.viewModel = new PacienteViewModel();

        // Carga los datos iniciales
        this.cargarPacientes();
    }


    /**
     * Llama al Interactor para obtener la lista de pacientes
     * y la guarda en el ViewModel.
     */
    public void cargarPacientes() {
        this.viewModel.setEstaCargando(true);
        try {
            List<Paciente> pacientes = this.pacienteInteractor.consultarPacientes();
            this.viewModel.setListaPacientes(pacientes);
            this.viewModel.setMensajeError(null);
        } catch (Exception e) {
            this.viewModel.setMensajeError("Error al cargar pacientes: " + e.getMessage());
            this.viewModel.setListaPacientes(List.of()); // Lista vacía en caso de error
        }
        this.viewModel.setEstaCargando(false);
    }

    /**
     * Prepara el ViewModel para un nuevo paciente
     * (Llamado por el botón "Nuevo")
     */
    public void nuevo() {
        this.viewModel.setPacienteSeleccionado(new Paciente());
    }

    /**
     * Llama al Interactor para guardar o actualizar un paciente.
     */
    public void guardar() {
        Paciente paciente = this.viewModel.getPacienteSeleccionado();
        boolean resultado;
        String mensaje;

        try {
            if (paciente.getId_paciente() == 0) {
                // Es Nuevo
                resultado = this.pacienteInteractor.registrarPaciente(paciente);
                mensaje = resultado ? "Paciente registrado" : "Error al registrar";
            } else {
                // Es Actualización
                resultado = this.pacienteInteractor.actualizarPaciente(paciente);
                mensaje = resultado ? "Paciente actualizado" : "Error al actualizar";
            }

            if (resultado) {
                // Si fue exitoso, recarga la tabla y resetea el formulario
                this.cargarPacientes();
                this.nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    /**
     * Llama al Interactor para eliminar un paciente.
     * (Se llama desde el botón "Eliminar" de la tabla)
     */
    public void eliminar() {
        try {
            boolean resultado = this.pacienteInteractor.eliminarPaciente(this.viewModel.getPacienteSeleccionado().getId_paciente());

            if(resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Paciente eliminado");
                this.cargarPacientes(); // Recarga la tabla
                this.nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar al paciente");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    // 4. Getter para el ViewModel
    // Así es como el .xhtml accede a todos los datos.
    public PacienteViewModel getViewModel() {
        return viewModel;
    }

    // Método de ayuda para mostrar mensajes en JSF
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}