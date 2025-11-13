package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.Prescripcion;
import com.uth.gestionhospitalaria.data.Medicamento;

import java.util.ArrayList;
import java.util.List;

public class HistorialViewModel {

    private List<HistorialClinico> listaHistoriales;
    private HistorialClinico historialSeleccionado;
    private boolean estaCargando;
    private String mensajeError;

    /** Almacena las prescripciones del historial seleccionado */
    private List<Prescripcion> prescripcionesDelHistorial;

    /** Almacena la nueva prescripción que se está creando en el popup */
    private Prescripcion prescripcionNueva;

    /** Almacena el catálogo de medicamentos para un dropdown */
    private List<Medicamento> catalogoMedicamentos;


    public HistorialViewModel() {
        this.listaHistoriales = new ArrayList<>();
        this.historialSeleccionado = new HistorialClinico();
        this.estaCargando = false;
        this.mensajeError = null;

        // --- Inicializar los nuevos campos ---
        this.prescripcionesDelHistorial = new ArrayList<>();
        this.prescripcionNueva = new Prescripcion();
        this.catalogoMedicamentos = new ArrayList<>();
    }

    // --- Getters y Setters ---

    public List<HistorialClinico> getListaHistoriales() {
        return listaHistoriales;
    }

    public void setListaHistoriales(List<HistorialClinico> listaHistoriales) {
        this.listaHistoriales = listaHistoriales;
    }

    public HistorialClinico getHistorialSeleccionado() {
        return historialSeleccionado;
    }

    public void setHistorialSeleccionado(HistorialClinico historialSeleccionado) {
        this.historialSeleccionado = historialSeleccionado;
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

    // --- Getters/Setters para Prescripciones ---

    public List<Prescripcion> getPrescripcionesDelHistorial() {
        return prescripcionesDelHistorial;
    }

    public void setPrescripcionesDelHistorial(List<Prescripcion> prescripcionesDelHistorial) {
        this.prescripcionesDelHistorial = prescripcionesDelHistorial;
    }

    public Prescripcion getPrescripcionNueva() {
        return prescripcionNueva;
    }

    public void setPrescripcionNueva(Prescripcion prescripcionNueva) {
        this.prescripcionNueva = prescripcionNueva;
    }

    public List<Medicamento> getCatalogoMedicamentos() {
        return catalogoMedicamentos;
    }

    public void setCatalogoMedicamentos(List<Medicamento> catalogoMedicamentos) {
        this.catalogoMedicamentos = catalogoMedicamentos;
    }
}