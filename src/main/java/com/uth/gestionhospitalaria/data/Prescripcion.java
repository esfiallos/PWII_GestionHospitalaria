package com.uth.gestionhospitalaria.data;

public class Prescripcion {
    private int id_prescripcion;
    private int id_historial_fk;
    private int id_medicamento_fk;
    private int cantidad;
    private String dosis;

    // Constructor vacío
    public Prescripcion() {
    }

    public int getId_prescripcion() {
        return id_prescripcion;
    }

    public void setId_prescripcion(int id_prescripcion) {
        this.id_prescripcion = id_prescripcion;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_medicamento_fk() {
        return id_medicamento_fk;
    }

    public void setId_medicamento_fk(int id_medicamento_fk) {
        this.id_medicamento_fk = id_medicamento_fk;
    }

    public int getId_historial_fk() {
        return id_historial_fk;
    }

    public void setId_historial_fk(int id_historial_fk) {
        this.id_historial_fk = id_historial_fk;
    }
}
