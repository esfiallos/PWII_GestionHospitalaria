package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.Paciente;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel para la pantalla de Pacientes.
 * Esta clase almacena el ESTADO de la vista (los datos que se muestran,
 * el estado de carga, errores, etc.)
 */
public class PacienteViewModel {

    private List<Paciente> listaPacientes;

    private Paciente pacienteSeleccionado;

    private boolean estaCargando;
    private String mensajeError;

    /**
     * Constructor
     * Inicializa el estado por defecto.
     */
    public PacienteViewModel() {
        this.listaPacientes = new ArrayList<>(); // Empezar con una lista vacía
        this.pacienteSeleccionado = new Paciente(); // Empezar con un objeto nuevo
        this.estaCargando = false;
        this.mensajeError = null;
    }

    // --- Getters y Setters para todas las propiedades ---
    // El JSF Bean (PacienteBean) los usará para leer y escribir.

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(Paciente pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public boolean isEstaCargando() {
        return estaCargando;
    }

    public void setEstaCargando(boolean estaCargando) {
        this.estaCargando = estaCargando;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}