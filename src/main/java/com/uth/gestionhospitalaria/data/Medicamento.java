package com.uth.gestionhospitalaria.data;

public class Medicamento {private int id_medicamento;
    private String nombre_comercial;
    private String principio_activo;
    private int stock;
    private double precio_unitario; // Usamos double para NUMBER(10, 2)

    // Constructor vacío
    public Medicamento() {
    }

    public int getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(int id_medicamento) {
        this.id_medicamento = id_medicamento;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getPrincipio_activo() {
        return principio_activo;
    }

    public void setPrincipio_activo(String principio_activo) {
        this.principio_activo = principio_activo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
