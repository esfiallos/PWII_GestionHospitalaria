package com.uth.gestionhospitalaria.data;

public class Factura {

    private int id_factura;
    private int id_cita_fk;
    private int id_paciente_fk;
    private String fecha_emision; // String es más simple
    private double monto_total;
    private String estado_pago;

    // Constructor vacío
    public Factura() {
    }

    public int getId_cita_fk() {
        return id_cita_fk;
    }

    public void setId_cita_fk(int id_cita_fk) {
        this.id_cita_fk = id_cita_fk;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public int getId_paciente_fk() {
        return id_paciente_fk;
    }

    public void setId_paciente_fk(int id_paciente_fk) {
        this.id_paciente_fk = id_paciente_fk;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(double monto_total) {
        this.monto_total = monto_total;
    }

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }
}
