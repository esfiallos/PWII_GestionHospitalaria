package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.*;
import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.*;
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

import java.util.Set;

@Named("historialBean")
@ViewScoped
public class HistorialBean implements Serializable {

    private HistorialInteractorImpl historialInteractor;
    private PrescripcionInteractorImpl prescripcionInteractor;
    private HistorialViewModel historialViewModel;

    private IPacienteInteractor pacienteInteractor;
    private IMedicamentoInteractor medicamentoInteractor;

    private ICitaInteractor citaInteractor;

    private Map<Integer, String> mapaPacientes;
    private Map<Integer, String> mapaMedicamentos;

    private List<CitaMedica> listaCitasCargadas;

    @PostConstruct
    public void init() {
        this.historialInteractor = new HistorialInteractorImpl();
        this.prescripcionInteractor = new PrescripcionInteractorImpl();
        this.historialViewModel = new HistorialViewModel();

        this.pacienteInteractor = new PacienteInteractorImpl();
        this.medicamentoInteractor = new MedicamentoInteractorImpl();
        this.citaInteractor = new CitaInteractorImpl();

        cargarDatosRelacionados();

        cargarHistoriales();
    }

    private void cargarDatosRelacionados() {
        try {
            // Cargar Pacientes y Medicamentos (como ya estaba)
            List<Paciente> pacientes = pacienteInteractor.consultarPacientes();
            mapaPacientes = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId_paciente, p -> p.getNombre() + " " + p.getApellido()));

            List<Medicamento> medicamentos = medicamentoInteractor.consultarMedicamentos();
            historialViewModel.setCatalogoMedicamentos(medicamentos);
            mapaMedicamentos = medicamentos.stream()
                    .collect(Collectors.toMap(Medicamento::getId_medicamento, Medicamento::getNombre_comercial));

            // --- LÓGICA MEJORADA ---
            // 1. Cargar todas las citas y todos los historiales
            this.listaCitasCargadas = citaInteractor.consultarCitas();
            List<HistorialClinico> historialesActuales = historialInteractor.consultarHistoriales();
            historialViewModel.setListaHistoriales(historialesActuales); // <-- Actualiza la lista principal

            // 2. Obtener los IDs de las citas que YA tienen un historial
            Set<Integer> idsCitasConHistorial = historialesActuales.stream()
                    .map(HistorialClinico::getId_cita_fk)
                    .collect(Collectors.toSet());

            // 3. Filtrar la lista de citas
            List<CitaMedica> citasSinHistorial = this.listaCitasCargadas.stream()
                    .filter(cita -> "COMPLETADA".equalsIgnoreCase(cita.getEstado_cita())) // (O 'PROGRAMADA' si se arregló)
                    .filter(cita -> !idsCitasConHistorial.contains(cita.getId_cita())) // <-- Solo las que NO tienen historial
                    .collect(Collectors.toList());

            // 4. Guardar la lista filtrada en el ViewModel
            historialViewModel.setListaCitasParaHistorial(citasSinHistorial);


        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar datos (Pacientes/Medicamentos/Citas)");
        }
    }

    public void onCitaChange() {
        int idCitaSeleccionada = historialViewModel.getHistorialSeleccionado().getId_cita_fk();

        if (idCitaSeleccionada == 0) return;

        CitaMedica citaSeleccionada = this.listaCitasCargadas.stream()
                .filter(c -> c.getId_cita() == idCitaSeleccionada)
                .findFirst()
                .orElse(null);

        if (citaSeleccionada != null) {
            historialViewModel.getHistorialSeleccionado().setId_paciente_fk(citaSeleccionada.getId_paciente_fk());
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


        boolean esNuevo = historial.getId_historial() == 0;


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