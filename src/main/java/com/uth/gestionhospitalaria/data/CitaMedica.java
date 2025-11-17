package com.uth.gestionhospitalaria.data;

import java.util.Date;

public class CitaMedica {
    private int id_cita;
    private int id_paciente_fk;
    private int id_doctor_fk;
    private Date fecha_hora_cita;
    private String estado_cita;
    private String motivo_consulta;

    // Constructor vacío
    public CitaMedica() {
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public String getEstado_cita() {
        return estado_cita;
    }

    public void setEstado_cita(String estado_cita) {
        this.estado_cita = estado_cita;
    }

    public String getMotivo_consulta() {
        return motivo_consulta;
    }

    public void setMotivo_consulta(String motivo_consulta) {
        this.motivo_consulta = motivo_consulta;
    }

    public Date getFecha_hora_cita() {
        return fecha_hora_cita;
    }

    public void setFecha_hora_cita(Date fecha_hora_cita) {
        this.fecha_hora_cita = fecha_hora_cita;
    }

    public int getId_doctor_fk() {
        return id_doctor_fk;
    }

    public void setId_doctor_fk(int id_doctor_fk) {
        this.id_doctor_fk = id_doctor_fk;
    }

    public int getId_paciente_fk() {
        return id_paciente_fk;
    }

    public void setId_paciente_fk(int id_paciente_fk) {
        this.id_paciente_fk = id_paciente_fk;
    }
}
