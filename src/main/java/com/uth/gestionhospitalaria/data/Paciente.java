package com.uth.gestionhospitalaria.data;

public class Paciente {
    private int id_paciente;
    private String dni;
    private String nombre;
    private String apellido;
    private String fecha_nacimiento;
    private String telefono;
    private String direccion;

    // Constructor vacío (Obligatorio para GSON)
    public Paciente() {
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
