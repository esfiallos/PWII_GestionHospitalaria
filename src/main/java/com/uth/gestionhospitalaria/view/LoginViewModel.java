package com.uth.gestionhospitalaria.view;

import com.uth.gestionhospitalaria.data.Usuario;

public class LoginViewModel {

    private String dni;
    private String password;

    //  Estado
    private boolean estaCargando;
    private String mensajeError;

    //  Resultado
    private Usuario usuarioLogueado;

    public LoginViewModel() {
        this.dni = "";
        this.password = "";
        this.estaCargando = false;
        this.mensajeError = null;
        this.usuarioLogueado = null;
    }

    // --- Getters y Setters ---

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}