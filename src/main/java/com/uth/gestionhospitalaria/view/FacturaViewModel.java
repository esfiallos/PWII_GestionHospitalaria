package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.Factura;
import java.util.ArrayList;
import java.util.List;

public class FacturaViewModel {

    private List<Factura> listaFacturas;
    private Factura facturaSeleccionada;
    private boolean estaCargando;
    private String mensajeError;

    public FacturaViewModel() {
        this.listaFacturas = new ArrayList<>();
        this.facturaSeleccionada = new Factura();
        this.estaCargando = false;
        this.mensajeError = null;
    }

    // --- Getters y Setters ---

    public List<Factura> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<Factura> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    public Factura getFacturaSeleccionada() {
        return facturaSeleccionada;
    }

    public void setFacturaSeleccionada(Factura facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
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