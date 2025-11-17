package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.HistorialInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.MedicamentoInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PrescripcionInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.HistorialClinico;
import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.data.Prescripcion;
import com.uth.gestionhospitalaria.view.HistorialViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("historialBean")
@ViewScoped
public class HistorialBean implements Serializable {

    private HistorialInteractorImpl historialInteractor;
    private PrescripcionInteractorImpl prescripcionInteractor;
    private HistorialViewModel historialViewModel;

    private IPacienteInteractor pacienteInteractor;
    private IMedicamentoInteractor medicamentoInteractor;

    private Map<Integer, String> mapaPacientes;
    private Map<Integer, String> mapaMedicamentos;

    @PostConstruct
    public void init() {
        this.historialInteractor = new HistorialInteractorImpl();
        this.prescripcionInteractor = new PrescripcionInteractorImpl();
        this.historialViewModel = new HistorialViewModel();

        this.pacienteInteractor = new PacienteInteractorImpl();
        this.medicamentoInteractor = new MedicamentoInteractorImpl();
        cargarDatosRelacionados();

        cargarHistoriales();
    }

    private void cargarDatosRelacionados() {
        try {
            List<Paciente> pacientes = pacienteInteractor.consultarPacientes();
            mapaPacientes = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId_paciente, p -> p.getNombre() + " " + p.getApellido()));

            List<Medicamento> medicamentos = medicamentoInteractor.consultarMedicamentos();
            historialViewModel.setCatalogoMedicamentos(medicamentos);
            mapaMedicamentos = medicamentos.stream()
                    .collect(Collectors.toMap(Medicamento::getId_medicamento, Medicamento::getNombre_comercial));

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar datos (Pacientes/Medicamentos)");


            mapaPacientes = java.util.Collections.emptyMap();
            mapaMedicamentos = java.util.Collections.emptyMap();

            if (historialViewModel != null) {
                historialViewModel.setCatalogoMedicamentos(java.util.Collections.emptyList());
            }
        }
    }

    public String getNombrePaciente(int idPaciente) {
        return mapaPacientes.getOrDefault(idPaciente, "ID: " + idPaciente);
    }

    public String getNombreMedicamento(int idMedicamento) {
        return mapaMedicamentos.getOrDefault(idMedicamento, "ID: " + idMedicamento);
    }

    public Map<Integer, String> getMapaPacientes() {
        return mapaPacientes;
    }


    public void cargarHistoriales(){
        historialViewModel.setEstaCargando(true);
        try {
            List<HistorialClinico> historial = historialInteractor.consultarHistoriales();
            historialViewModel.setListaHistoriales(historial);
            historialViewModel.setMensajeError(null);
        }catch (Exception ex){
            historialViewModel.setMensajeError(ex.getMessage());
            historialViewModel.setListaHistoriales(List.of());
        }finally {
            historialViewModel.setEstaCargando(false);
        }
    }

    public void nuevoHistorial(){
        historialViewModel.setHistorialSeleccionado(new HistorialClinico());
        historialViewModel.setPrescripcionesDelHistorial(List.of());
        historialViewModel.setPrescripcionNueva(new Prescripcion());
    }

    public void seleccionarHistorial(HistorialClinico historial){
        historialViewModel.setHistorialSeleccionado(historial);
        cargarPrescripciones(historial.getId_historial());
    }

    public void cargarPrescripciones(int idHistorial){
        if (idHistorial == 0) {
            historialViewModel.setPrescripcionesDelHistorial(List.of());
            return;
        }

        try {
            List<Prescripcion> todas = prescripcionInteractor.consultarPrescripciones();
            List<Prescripcion> filtrar = todas.stream().filter(p -> p.getId_historial_fk() == idHistorial).toList();
            historialViewModel.setPrescripcionesDelHistorial(filtrar);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las prescripciones");
            historialViewModel.setPrescripcionesDelHistorial(List.of());
        }
    }

    public void guardarHistorial(){
        HistorialClinico historial = historialViewModel.getHistorialSeleccionado();
        boolean resultado;
        String mensaje;

        // --- INICIO DE LA CORRECCIÓN ---
        // Mantenemos un registro de si era un nuevo historial
        boolean esNuevo = historial.getId_historial() == 0;
        // --- FIN DE LA CORRECCIÓN ---

        try {
            if (esNuevo) {
                resultado = historialInteractor.registrarEnHistorial(historial);
                mensaje = resultado ? "Historial registrado" : "Error al registrar";
            } else {
                resultado = historialInteractor.actualizarHistorial(historial);
                mensaje = resultado ? "Historial actualizado" : "Error al actualizar";
            }

            if (resultado) {
                cargarHistoriales();


                if (esNuevo) {
                    mensaje += ". Cierre este diálogo y edite el nuevo registro para agregar prescripciones.";
                }


                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", e.getMessage());
        }
    }

    public void agregarPrescripcion() {
        Prescripcion nueva = historialViewModel.getPrescripcionNueva();
        nueva.setId_historial_fk(historialViewModel.getHistorialSeleccionado().getId_historial());

        boolean resultado = prescripcionInteractor.registrarPrescripcion(nueva);
        if (resultado) {
            cargarPrescripciones(nueva.getId_historial_fk());
            historialViewModel.setPrescripcionNueva(new Prescripcion());
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Prescripción agregada");
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo agregar la prescripción");
        }
    }

    public void eliminarPrescripcion(Prescripcion prescripcion) {
        boolean resultado = prescripcionInteractor.eliminarPrescripcion(prescripcion.getId_prescripcion());
        if (resultado) {
            cargarPrescripciones(prescripcion.getId_historial_fk());
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Prescripción eliminada");
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la prescripción");
        }
    }

    public HistorialViewModel getHistorialViewModel() {
        return historialViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}