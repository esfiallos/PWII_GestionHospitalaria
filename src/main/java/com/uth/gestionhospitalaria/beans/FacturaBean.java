package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.FacturaInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IFacturaInteractor;
import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.view.FacturaViewModel;

import com.uth.gestionhospitalaria.controller.Implements.PacienteInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IPacienteInteractor;
import com.uth.gestionhospitalaria.data.Paciente;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("facturaBean")
@ViewScoped
public class FacturaBean implements Serializable {
    private IFacturaInteractor facturaInteractor;
    private FacturaViewModel facturaViewModel;

    private IPacienteInteractor pacienteInteractor;
    private Map<Integer, String> mapaPacientes;


    @PostConstruct
    public void init() {
        this.facturaInteractor = new FacturaInteractorImpl();
        this.facturaViewModel = new FacturaViewModel();

        this.pacienteInteractor = new PacienteInteractorImpl();
        cargarDatosRelacionados();

        cargarFacturas();
    }


    private void cargarDatosRelacionados() {
        try {
            List<Paciente> pacientes = pacienteInteractor.consultarPacientes();
            mapaPacientes = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId_paciente, p -> p.getNombre() + " " + p.getApellido()));

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar datos (Pacientes)");
        }
    }


    public String getNombrePaciente(int idPaciente) {
        return mapaPacientes.getOrDefault(idPaciente, "ID: " + idPaciente);
    }


    public Map<Integer, String> getMapaPacientes() {
        return mapaPacientes;
    }


    public void cargarFacturas() {
        facturaViewModel.setEstaCargando(true);
        try{
            List<Factura> factura = facturaInteractor.consultarFacturas();
            facturaViewModel.setListaFacturas(factura);
            facturaViewModel.setMensajeError(null);
        }catch(Exception ex){
            facturaViewModel.setMensajeError("Error al cargar facturas: " + ex.getMessage());
            facturaViewModel.setListaFacturas(List.of());
        }
        facturaViewModel.setEstaCargando(false);
    }

    public void nuevo(){
        facturaViewModel.setFacturaSeleccionada(new Factura());
    }

    public void guardar(){
        Factura factura = facturaViewModel.getFacturaSeleccionada();
        boolean resultado;
        String mensaje;

        try{
            if (factura.getId_factura() == 0) {
                resultado = facturaInteractor.generarFactura(factura);
                mensaje = resultado ? "Factura generada" : "Error al generar la factura";
            } else {

                resultado = facturaInteractor.actualizarFactura(factura);
                mensaje = resultado ? "Factura actualizada" : "Error al actualizar la factura";

                if(resultado) {
                    resultado = facturaInteractor.consultarFacturaPorId(factura.getId_factura()) != null &&
                            facturaInteractor.marcarComoPagada(factura.getId_factura());
                    mensaje = resultado ? "Factura actualizada" : "Error al actualizar la factura";
                } else {
                    mensaje = "Factura no encontrada";
                }
            }

            if (resultado) {
                cargarFacturas();
                nuevo();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
            }

        }catch(Exception ex){
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public void eliminar() {
        Factura factura = facturaViewModel.getFacturaSeleccionada();
        if(factura == null || factura.getId_factura() == 0) {
            addMessage(FacesMessage.SEVERITY_WARN, "Atención", "Seleccione una factura para eliminar");
            return;
        }

        try {
            boolean resultado = facturaInteractor.consultarFacturaPorId(factura.getId_factura()) != null &&
                    facturaInteractor.consultarFacturaPorId(factura.getId_factura()) != null;
            if(resultado) {
                resultado = facturaInteractor.consultarFacturaPorId(factura.getId_factura()) != null &&
                        facturaInteractor.marcarComoPagada(factura.getId_factura());
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Factura eliminada");
                cargarFacturas();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la factura");
            }
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public void marcarComoPagada(Factura factura) {
        try {
            boolean resultado = facturaInteractor.marcarComoPagada(factura.getId_factura());
            if(resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Factura marcada como pagada");
                cargarFacturas();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo marcar la factura como pagada");
            }
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public FacturaViewModel getFacturaViewModel() {
        return facturaViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}