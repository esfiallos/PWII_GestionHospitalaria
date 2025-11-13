package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.data.Usuario; // Para la lista de doctores
import java.util.ArrayList;
import java.util.List;

public class CitaViewModel {

    private List<CitaMedica> listaCitas;
    private CitaMedica citaSeleccionada;
    private boolean estaCargando;
    private String mensajeError;

    private List<Paciente> listaPacientesDropdown;
    private List<Usuario> listaDoctoresDropdown;

    public CitaViewModel() {
        this.listaCitas = new ArrayList<>();
        this.citaSeleccionada = new CitaMedica();
        this.estaCargando = false;
        this.mensajeError = null;
        this.listaPacientesDropdown = new ArrayList<>();
        this.listaDoctoresDropdown = new ArrayList<>();
    }

    // --- Getters y Setters ---

    public List<CitaMedica> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<CitaMedica> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public CitaMedica getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public void setCitaSeleccionada(CitaMedica citaSeleccionada) {
        this.citaSeleccionada = citaSeleccionada;
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

    public List<Paciente> getListaPacientesDropdown() {
        return listaPacientesDropdown;
    }

    public void setListaPacientesDropdown(List<Paciente> listaPacientesDropdown) {
        this.listaPacientesDropdown = listaPacientesDropdown;
    }

    public List<Usuario> getListaDoctoresDropdown() {
        return listaDoctoresDropdown;
    }

    public void setListaDoctoresDropdown(List<Usuario> listaDoctoresDropdown) {
        this.listaDoctoresDropdown = listaDoctoresDropdown;
    }
}