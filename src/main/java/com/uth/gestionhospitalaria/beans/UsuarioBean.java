package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.UsuarioViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {
    private IUsuarioInteractor usuarioInteractor;
    private UsuarioViewModel  usuarioViewModel;

    @PostConstruct
    public void init(){
        this.usuarioInteractor = new UsuarioInteractorImpl();
        this.usuarioViewModel = new UsuarioViewModel();
    }

    public void cargarUsuarios() {
        usuarioViewModel.setEstaCargando(true);
        try {
            List<Usuario> usuarios = usuarioInteractor.consultarUsuarios();
            usuarioViewModel.setListaUsuarios(usuarios);
            usuarioViewModel.setMensajeError(null);
        } catch (Exception e) {
            usuarioViewModel.setListaUsuarios(List.of());
            usuarioViewModel.setMensajeError("Error al cargar usuarios: " + e.getMessage());
        }
        usuarioViewModel.setEstaCargando(false);
    }

    public void nuevo() {
        usuarioViewModel.setUsuarioSeleccionado(new Usuario());
    }

    public void guardar() {
        Usuario u = usuarioViewModel.getUsuarioSeleccionado();
        boolean resultado;
        String mensaje;
        try {
            if (u.getId_usuario() == 0) {
                resultado = usuarioInteractor.registrarUsuario(u);
                mensaje = resultado ? "Usuario registrado" : "Error al registrar";
            } else {
                resultado = usuarioInteractor.actualizarUsuario(u);
                mensaje = resultado ? "Usuario actualizado" : "Error al actualizar";
            }

            if (resultado) {
                cargarUsuarios();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public void eliminar() {
        try {

            Usuario usuario = usuarioViewModel.getUsuarioSeleccionado();

            usuario.setEstado("INACTIVO");

            boolean resultado = usuarioInteractor.actualizarUsuario(usuario);

            if (resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario desactivado correctamente");
                cargarUsuarios();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo desactivar el usuario");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public UsuarioViewModel getViewModel() {
        return usuarioViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
