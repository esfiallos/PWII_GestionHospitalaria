package com.uth.gestionhospitalaria.data;

public class HistorialClinico {

    private int id_historial;
    private int id_cita_fk;
    private int id_paciente_fk;
    private String diagnostico;
    private String notas_doctor;

    // Constructor vacío
    public HistorialClinico() {
    }

    public int getId_historial() {
        return id_historial;
    }

    public void setId_historial(int id_historial) {
        this.id_historial = id_historial;
    }

    public int getId_cita_fk() {
        return id_cita_fk;
    }

    public void setId_cita_fk(int id_cita_fk) {
        this.id_cita_fk = id_cita_fk;
    }

    public int getId_paciente_fk() {
        return id_paciente_fk;
    }

    public void setId_paciente_fk(int id_paciente_fk) {
        this.id_paciente_fk = id_paciente_fk;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getNotas_doctor() {
        return notas_doctor;
    }

    public void setNotas_doctor(String notas_doctor) {
        this.notas_doctor = notas_doctor;
    }
}
