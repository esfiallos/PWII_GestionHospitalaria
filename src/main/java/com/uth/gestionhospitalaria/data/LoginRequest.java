package com.uth.gestionhospitalaria.data;

public class LoginRequest {
    // Los nombres deben coincidir con las variables
    // que espera tu handler de ORDS (:dni, :password)
    private String dni;
    private String password;


    public LoginRequest(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }

    // --- Getters ---

    public String getDni() {
        return dni;
    }

    public String getPassword() {
        return password;
    }
}
