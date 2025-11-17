package com.uth.gestionhospitalaria.beans;

// Imports de tu proyecto
import com.uth.gestionhospitalaria.controller.Implements.CitaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Paciente;
import com.uth.gestionhospitalaria.data.Usuario;

// Imports de Jakarta y Java
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {


    private int totalPacientes;
    private long totalDoctores;
    private long totalRecepcionistas;
    private Map<String, Long> conteoPorEstado;

    // Modelo para la tabla
    private List<CitaMedica> proximasCitas;

    // Dependencias
    private IPacienteInteractor pacienteInteractor;
    private ICitaInteractor citaInteractor;
    private IUsuarioInteractor usuarioInteractor;


    private Map<Integer, String> mapaPacientes;
    private Map<Integer, String> mapaDoctores;

    public DashboardBean() {
        this.pacienteInteractor = new PacienteInteractorImpl();
        this.citaInteractor = new CitaInteractorImpl();
        this.usuarioInteractor = new UsuarioInteractorImpl();
    }

    @PostConstruct
    public void init() {

        cargarDatosRelacionados();


        cargarEstadisticasCitas();
        cargarEstadisticasGenerales();


        cargarProximasCitas();
    }

    private void cargarDatosRelacionados() {
        try {
            List<Paciente> pacientes = pacienteInteractor.consultarPacientes();
            mapaPacientes = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId_paciente, p -> p.getNombre() + " " + p.getApellido()));

            List<Usuario> doctores = usuarioInteractor.consultarUsuarios().stream()
                    .filter(u -> "DOCTOR".equalsIgnoreCase(u.getRol()))
                    .toList();
            mapaDoctores = doctores.stream()
                    .collect(Collectors.toMap(Usuario::getId_usuario, u -> u.getNombre() + " " + u.getApellido()));

        } catch (Exception e) {
            System.err.println("Error cargando mapas para el Dashboard: " + e.getMessage());
            mapaPacientes = Collections.emptyMap();
            mapaDoctores = Collections.emptyMap();
        }
    }

    private void cargarEstadisticasCitas() {
        try {
            List<CitaMedica> citas = citaInteractor.consultarCitas();
            this.conteoPorEstado = citas.stream()
                    .filter(c -> c.getEstado_cita() != null)
                    .collect(Collectors.groupingBy(CitaMedica::getEstado_cita, Collectors.counting()));
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas de citas: " + e.getMessage());
            this.conteoPorEstado = Collections.emptyMap();
        }
    }

    private void cargarEstadisticasGenerales() {
        try {
            this.totalPacientes = pacienteInteractor.consultarPacientes().size();
            List<Usuario> usuarios = usuarioInteractor.consultarUsuarios();
            this.totalDoctores = usuarios.stream().filter(u -> "DOCTOR".equalsIgnoreCase(u.getRol())).count();
            this.totalRecepcionistas = usuarios.stream().filter(u -> "RECEPCIONISTA".equalsIgnoreCase(u.getRol())).count();
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas generales: " + e.getMessage());
            this.totalPacientes = 0;
            this.totalDoctores = 0;
            this.totalRecepcionistas = 0;
        }
    }

    private void cargarProximasCitas() {
        try {
            List<CitaMedica> todasLasCitas = citaInteractor.consultarCitas();
            LocalDateTime ahora = LocalDateTime.now();

            this.proximasCitas = todasLasCitas.stream()
                    .map(cita -> {
                        try {
                            java.util.Date fechaComoDate = cita.getFecha_hora_cita();
                            if (fechaComoDate == null) {
                                return null;
                            }
                            LocalDateTime ldt = fechaComoDate.toInstant()
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDateTime();
                            return Map.entry(cita, ldt);
                        } catch (Exception e) {
                            System.err.println("Error convirtiendo fecha para cita ID: " + cita.getId_cita());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(entry -> {
                        CitaMedica cita = entry.getKey();
                        LocalDateTime fechaCita = entry.getValue();
                        boolean isCancelled = "CANCELADA".equalsIgnoreCase(cita.getEstado_cita());
                        boolean isFutureOrToday = !fechaCita.toLocalDate().isBefore(ahora.toLocalDate());
                        return !isCancelled && isFutureOrToday;
                    })
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            this.proximasCitas = Collections.emptyList();
        }
    }


    public int getTotalPacientes() {
        return totalPacientes;
    }

    public long getTotalDoctores() {
        return totalDoctores;
    }

    public long getTotalRecepcionistas() {
        return totalRecepcionistas;
    }

    public Map<String, Long> getConteoPorEstado() {
        return conteoPorEstado;
    }

    public long getCitasAgendadas() {
        return conteoPorEstado.getOrDefault("AGENDADA", 0L);
    }

    public long getCitasCompletadas() {
        return conteoPorEstado.getOrDefault("COMPLETADA", 0L);
    }

    public long getCitasCanceladas() {
        return conteoPorEstado.getOrDefault("CANCELADA", 0L);
    }

    // --- Getters para la Tabla (igual que antes) ---
    public List<CitaMedica> getProximasCitas() {
        return proximasCitas;
    }

    public String getNombrePaciente(int idPaciente) {
        return mapaPacientes.getOrDefault(idPaciente, "ID: " + idPaciente);
    }

    public String getNombreDoctor(int idDoctor) {
        return mapaDoctores.getOrDefault(idDoctor, "ID: " + idDoctor);
    }
}