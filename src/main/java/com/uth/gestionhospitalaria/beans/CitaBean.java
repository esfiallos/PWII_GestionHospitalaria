package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.CitaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.FacturaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IFacturaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.view.CitaViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("citaBean")
@ViewScoped
public class CitaBean implements Serializable {
    private ICitaInteractor citaInteractor;
    private CitaViewModel citaViewModel;

    private IPacienteInteractor pacienteInteractor;
    private IUsuarioInteractor usuarioInteractor;

    private IFacturaInteractor facturaInteractor;

    private Map<Integer, String> mapaPacientes;
    private Map<Integer, String> mapaDoctores;


    @PostConstruct
    public void init() {
        this.citaInteractor = new CitaInteractorImpl();
        this.citaViewModel = new CitaViewModel();
        this.pacienteInteractor = new PacienteInteractorImpl();
        this.usuarioInteractor = new UsuarioInteractorImpl();

        this.facturaInteractor = new FacturaInteractorImpl();

        cargarDatosRelacionados();

        this.cargarCita();
    }

    private void cargarDatosRelacionados() {
        try {
            List<Paciente> pacientes = pacienteInteractor.consultarPacientes();
            citaViewModel.setListaPacientesDropdown(pacientes);

            List<Usuario> doctores = usuarioInteractor.consultarUsuarios().stream()
                    .filter(u -> "DOCTOR".equalsIgnoreCase(u.getRol()))
                    .collect(Collectors.toList());
            citaViewModel.setListaDoctoresDropdown(doctores);

            mapaPacientes = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId_paciente, p -> p.getNombre() + " " + p.getApellido()));

            mapaDoctores = doctores.stream()
                    .collect(Collectors.toMap(Usuario::getId_usuario, u -> u.getNombre() + " " + u.getApellido()));

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar datos (Pacientes/Doctores)");
        }
    }

    public String getNombrePaciente(int idPaciente) {
        return mapaPacientes.getOrDefault(idPaciente, "ID: " + idPaciente);
    }

    public String getNombreDoctor(int idDoctor) {
        return mapaDoctores.getOrDefault(idDoctor, "ID: " + idDoctor);
    }

    public void cargarCita() {
        citaViewModel.setEstaCargando(true);
        try{
            List<CitaMedica> cita = citaInteractor.consultarCitas();
            citaViewModel.setListaCitas(cita);
            citaViewModel.setMensajeError(null);
        }catch(Exception ex){
            citaViewModel.setMensajeError("Error" + ex.getMessage());
            citaViewModel.setListaCitas(List.of());
        }
        citaViewModel.setEstaCargando(false);
    }



    public void nuevo(){
        citaViewModel.setCitaSeleccionada(new CitaMedica());
    }

    public void guardar(){
        CitaMedica cita = citaViewModel.getCitaSeleccionada();
        boolean resultado;
        String mensaje;

        try{
            if(cita.getId_cita() == 0) {
                // Lógica para crear nueva cita (se mantiene igual)
                resultado = this.citaInteractor.agendarCita(cita);
                mensaje = resultado ? "Cita creada con éxito" : "Error al crear la cita";

            }else{
                // --- LÓGICA DE ACTUALIZACIÓN (MODIFICADA) ---

                // 1. Guardar el estado anterior antes de actualizar
                CitaMedica citaAntesDeActualizar = citaInteractor.consultarCitaPorId(cita.getId_cita());
                String estadoAnterior = (citaAntesDeActualizar != null) ? citaAntesDeActualizar.getEstado_cita() : "";

                // 2. Actualizar la cita
                resultado = citaInteractor.actualizarCita(cita);
                mensaje = resultado ? "Cita actualizada con éxito" : "Error al actualizar la cita";

                // 3. (NUEVO) Comprobar si se debe generar factura
                // Generamos factura si el estado cambió A "COMPLETADA"
                boolean seCompletoLaCita = resultado &&
                        !"COMPLETADA".equalsIgnoreCase(estadoAnterior) &&
                        "COMPLETADA".equalsIgnoreCase(cita.getEstado_cita());

                if(seCompletoLaCita) {
                    generarFacturaParaCita(cita);
                    mensaje += " y factura generada.";
                }
            }

            if(resultado){
                cargarCita();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }

        }catch(Exception ex){
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    private void generarFacturaParaCita(CitaMedica citaCompletada) {
        try {
            Factura nuevaFactura = new Factura();
            nuevaFactura.setId_cita_fk(citaCompletada.getId_cita());
            nuevaFactura.setId_paciente_fk(citaCompletada.getId_paciente_fk());

            nuevaFactura.setMonto_total(0);

            nuevaFactura.setFecha_emision(new Date());

            boolean facturaGenerada = this.facturaInteractor.generarFactura(nuevaFactura);

            if (!facturaGenerada) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La cita se completó, pero no se pudo generar la factura.");
            }

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error de Factura", "Error al intentar generar la factura: " + e.getMessage());
        }
    }

    public void eliminar(){
        try {
            boolean resultado = citaInteractor.cancelarCita(citaViewModel.getCitaSeleccionada().getId_cita());
            if(resultado){
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cita cancelada/eliminada");
                cargarCita();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cancelar la cita");
            }
        }catch(Exception ex) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public CitaViewModel getCitaViewModel() {
        return citaViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}