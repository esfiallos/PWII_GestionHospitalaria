package com.uth.gestionhospitalaria.beans;

// Imports de tu proyecto
import com.uth.gestionhospitalaria.controller.Implements.CitaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Implements.UsuarioInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.ICitaInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.CitaMedica;
import com.uth.gestionhospitalaria.data.Usuario;

// Imports de Jakarta y Java
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime; // <-- CAMBIO: Importar ZonedDateTime
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects; // <-- CAMBIO: Importar Objects
import java.util.stream.Collectors;

// Imports de la librería de Charts
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.PieDataset;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.Options;
import software.xdev.chartjs.model.options.scale.Scales;
import software.xdev.chartjs.model.options.scale.cartesian.linear.LinearScaleOptions;

@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    // Modelos (Json Strings)
    private String pieModelJson;
    private String barModelJson;

    // Modelo para la tabla
    private List<CitaMedica> proximasCitas;

    // Dependencias
    private IPacienteInteractor pacienteInteractor;
    private ICitaInteractor citaInteractor;
    private IUsuarioInteractor usuarioInteractor;

    public DashboardBean() {
        this.pacienteInteractor = new PacienteInteractorImpl();
        this.citaInteractor = new CitaInteractorImpl();
        this.usuarioInteractor = new UsuarioInteractorImpl();
    }

    @PostConstruct
    public void init() {
        crearGraficaPieCitas();
        crearGraficaBarGeneral();
        cargarProximasCitas(); // <-- MÉTODO MODIFICADO
    }

    // ... (crearGraficaPieCitas y crearGraficaBarGeneral se mantienen igual que antes) ...

    private void crearGraficaPieCitas() {
        List<CitaMedica> citas = citaInteractor.consultarCitas();
        Map<String, Long> conteoPorEstado = citas.stream()
                .filter(c -> c.getEstado_cita() != null)
                .collect(Collectors.groupingBy(CitaMedica::getEstado_cita, Collectors.counting()));

        PieChart pieChart = new PieChart();
        PieData pieData = new PieData();
        PieDataset dataSet = new PieDataset();

        List<Number> valores = conteoPorEstado.values().stream()
                .map(Long::doubleValue)
                .collect(Collectors.toList());
        List<String> etiquetas = conteoPorEstado.keySet().stream().collect(Collectors.toList());

        dataSet.setData(valores.toArray(new Number[0]));
        dataSet.setBackgroundColor(List.of("rgb(54, 162, 235)", "rgb(75, 192, 192)", "rgb(255, 99, 132)"));

        pieData.setLabels(etiquetas.toArray(new String[0]));
        pieData.addDataset(dataSet);
        pieChart.setData(pieData);

        this.pieModelJson = pieChart.toJson();
    }

    private void crearGraficaBarGeneral() {
        int totalPacientes = pacienteInteractor.consultarPacientes().size();
        List<Usuario> usuarios = usuarioInteractor.consultarUsuarios();
        long totalDoctores = usuarios.stream().filter(u -> "DOCTOR".equalsIgnoreCase(u.getRol())).count();
        long totalRecepcionistas = usuarios.stream().filter(u -> "RECEPCIONISTA".equalsIgnoreCase(u.getRol())).count();

        BarChart barChart = new BarChart();
        BarData barData = new BarData();
        BarDataset barDataSet = new BarDataset().setLabel("Conteo Total");
        List<String> etiquetas = List.of("Pacientes", "Doctores", "Recepcionistas");

        barDataSet.setData(totalPacientes, totalDoctores, totalRecepcionistas);
        barDataSet.setBackgroundColor(List.of("rgba(54, 162, 235, 0.6)", "rgba(75, 192, 192, 0.6)", "rgba(255, 206, 86, 0.6)"));

        barData.setLabels(etiquetas.toArray(new String[0]));
        barData.addDataset(barDataSet);
        barChart.setData(barData);

        BarOptions options = new BarOptions();
        LinearScaleOptions yAxis = new LinearScaleOptions().setBeginAtZero(true);
        options.getScales().addScale(Scales.ScaleAxis.Y, yAxis);
        barChart.setOptions(options);

        this.barModelJson = barChart.toJson();
    }


    // --- MÉTODO CORREGIDO ---
    private void cargarProximasCitas() {
        try {
            List<CitaMedica> todasLasCitas = citaInteractor.consultarCitas();
            LocalDateTime ahora = LocalDateTime.now();

            this.proximasCitas = todasLasCitas.stream()
                    // 1. Convertir a un par de [Cita, LocalDateTime]
                    .map(cita -> {
                        try {
                            // ¡YA NO PARSEAMOS! Ahora convertimos el Date.
                            java.util.Date fechaComoDate = cita.getFecha_hora_cita();

                            // Si la fecha es nula en la BD, saltar
                            if (fechaComoDate == null) {
                                return null;
                            }

                            // Convertir java.util.Date a java.time.LocalDateTime
                            LocalDateTime ldt = fechaComoDate.toInstant()
                                    .atZone(java.time.ZoneId.systemDefault()) // Usa la zona horaria del servidor
                                    .toLocalDateTime();

                            return Map.entry(cita, ldt);

                        } catch (Exception e) {
                            // Error al convertir la fecha
                            System.err.println("Error convirtiendo fecha para cita ID: " + cita.getId_cita());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Quitar las entradas nulas
                    // 2. Filtrar canceladas y pasadas
                    .filter(entry -> {
                        CitaMedica cita = entry.getKey();
                        LocalDateTime fechaCita = entry.getValue();
                        boolean isCancelled = "CANCELADA".equalsIgnoreCase(cita.getEstado_cita());

                        // Compara si la cita es HOY o en el FUTURO
                        boolean isFutureOrToday = !fechaCita.toLocalDate().isBefore(ahora.toLocalDate());

                        return !isCancelled && isFutureOrToday;
                    })
                    // 3. Ordenar por fecha (más próxima primero)
                    .sorted(Map.Entry.comparingByValue())
                    // 4. Obtener solo el objeto CitaMedica
                    .map(Map.Entry::getKey)
                    // 5. Tomar las primeras 5
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            this.proximasCitas = Collections.emptyList();
        }
    }


    public String getPieModelJson() {
        return pieModelJson;
    }

    public String getBarModelJson() {
        return barModelJson;
    }

    public List<CitaMedica> getProximasCitas() {
        return proximasCitas;
    }
}