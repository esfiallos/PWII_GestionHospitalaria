package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.UsuarioViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    private IUsuarioInteractor usuarioInteractor;
    private UsuarioViewModel viewModel;

    @PostConstruct
    public void init() {
        this.usuarioInteractor = new UsuarioInteractorImpl();
        this.viewModel = new UsuarioViewModel();
        this.cargarUsuarios();
    }

    public UsuarioViewModel getViewModel() {
        return viewModel;
    }

    public void cargarUsuarios() {
        viewModel.setEstaCargando(true);
        try {
            viewModel.setListaUsuarios(usuarioInteractor.consultarUsuarios());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar usuarios");
        }
        viewModel.setEstaCargando(false);
    }

    public void nuevo() {
        viewModel.setUsuarioSeleccionado(new Usuario());
    }

    public void guardar() {
        Usuario usuario = viewModel.getUsuarioSeleccionado();
        boolean resultado;
        String mensaje;
        try {
            if (usuario.getId_usuario() == 0) {
                resultado = usuarioInteractor.registrarUsuario(usuario);
                mensaje = "Usuario registrado";
            } else {
                resultado = usuarioInteractor.actualizarUsuario(usuario);
                mensaje = "Usuario actualizado";
            }
            if (resultado) {
                cargarUsuarios();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el usuario");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public void eliminar() {
        try {
            boolean resultado = usuarioInteractor.eliminarUsuario(viewModel.getUsuarioSeleccionado().getId_usuario());
            if (resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado");
                cargarUsuarios();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}