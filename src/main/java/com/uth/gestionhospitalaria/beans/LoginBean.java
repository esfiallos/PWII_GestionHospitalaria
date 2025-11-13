package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.LoginViewModel;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    // Dependencias
    private IUsuarioInteractor usuarioInteractor;
    private LoginViewModel viewModel;

    @PostConstruct
    public void init() {
        this.usuarioInteractor = new UsuarioInteractorImpl();
        this.viewModel = new LoginViewModel();
    }

    // Getter para que la vista (.xhtml) acceda al estado
    public LoginViewModel getViewModel() {
        return viewModel;
    }

    // --- Acción de Botón ---
    public void login() {
        viewModel.setEstaCargando(true);
        viewModel.setMensajeError(null);
        try {
            // 1. Llamar al Interactor
            Usuario usuario = usuarioInteractor.autenticarUsuario(
                    viewModel.getDni(),
                    viewModel.getPassword()
            );

            // 2. Validar el resultado
            if (usuario != null && "ACTIVO".equals(usuario.getEstado())) {
                // ¡Éxito!
                viewModel.setUsuarioLogueado(usuario);

                // 3. Guardar el usuario en la Sesión HTTP
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("usuarioLogueado", usuario);

                addMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", usuario.getNombre());

                // 4. Redirigir a la página principal
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("index.xhtml"); // O la página de pacientes

            } else {
                // Fracaso
                viewModel.setMensajeError("DNI o contraseña incorrectos, o usuario inactivo.");
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Credenciales incorrectas");
            }
        } catch (Exception e) {
            viewModel.setMensajeError("Error del sistema: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_FATAL, "Error", "Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
        viewModel.setEstaCargando(false);
    }

    // --- Helper para mensajes ---
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}