package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioViewModel {

    private List<Usuario> listaUsuarios;
    private Usuario usuarioSeleccionado;
    private boolean estaCargando;
    private String mensajeError;

    public UsuarioViewModel() {
        this.listaUsuarios = new ArrayList<>();
        this.usuarioSeleccionado = new Usuario();
        this.estaCargando = false;
        this.mensajeError = null;
    }

    // --- Getters y Setters ---

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
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