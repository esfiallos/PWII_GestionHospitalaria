package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.Medicamento;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoViewModel {

    private List<Medicamento> listaMedicamentos;
    private Medicamento medicamentoSeleccionado;
    private boolean estaCargando;
    private String mensajeError;

    public MedicamentoViewModel() {
        this.listaMedicamentos = new ArrayList<>();
        this.medicamentoSeleccionado = new Medicamento();
        this.estaCargando = false;
        this.mensajeError = null;
    }

    // --- Getters y Setters ---

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public void setMedicamentoSeleccionado(Medicamento medicamentoSeleccionado) {
        this.medicamentoSeleccionado = medicamentoSeleccionado;
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