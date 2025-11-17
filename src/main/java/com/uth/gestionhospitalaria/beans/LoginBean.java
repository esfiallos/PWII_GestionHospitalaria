package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.LoginViewModel;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean  implements Serializable {
    private LoginViewModel loginViewModel;
    private final IUsuarioInteractor usuarioInteractor;

    public LoginBean(){
        this.loginViewModel = new LoginViewModel();
        this.usuarioInteractor = new UsuarioInteractorImpl();
    }

    public String login(){
        String dni = loginViewModel.getDni();
        String password = loginViewModel.getPassword();

        if(dni==null || dni.isBlank() || password==null ||  password.isBlank()){
            loginViewModel.setMensajeError("Ingrese DNI y contrasena");
            return null;
        }

        Usuario usuario = usuarioInteractor.autenticarUsuario(dni.trim(), password.trim());
        if(usuario==null){
            loginViewModel.setMensajeError("Uusario o contrasena incorrecta");
            return null;
        }

        loginViewModel.setUsuarioLogueado(usuario);
        loginViewModel.setMensajeError(null);

        String rol = usuario.getRol().toUpperCase();

        return switch (rol) {
            case "ADMIN", "RECEPCIONISTA", "DOCTOR" -> "dashboard?faces-redirect=true";
            default -> {
                loginViewModel.setMensajeError("Error: Rol no reconocido.");
                yield null;
            }
        };

    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
