package com.uth.gestionhospitalaria.data;

public class LoginRequest {
    // Los nombres deben coincidir con las variables
    // que espera tu handler de ORDS (:dni, :password)
    private String dni;
    private String password;

    /**
     * Constructor para crear el objeto de solicitud.
     * @param dni El DNI del usuario.
     * @param password La contraseña en texto plano.
     */
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
