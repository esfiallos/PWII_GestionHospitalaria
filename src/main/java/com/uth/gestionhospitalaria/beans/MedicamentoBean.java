package com.uth.gestionhospitalaria.beans;

import com.uth.gestionhospitalaria.controller.Implements.MedicamentoInteractorImpl;
import com.uth.gestionhospitalaria.controller.Interactor.IMedicamentoInteractor;
import com.uth.gestionhospitalaria.data.Medicamento;
import com.uth.gestionhospitalaria.view.MedicamentoViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("medicamentoBean")
@ViewScoped
public class MedicamentoBean implements Serializable {

    private IMedicamentoInteractor medicamentoInteractor;
    private MedicamentoViewModel medicamentoViewModel;

    @PostConstruct
    public void init() {
        this.medicamentoInteractor = new MedicamentoInteractorImpl();
        this.medicamentoViewModel = new MedicamentoViewModel();
        this.cargarMedicamentos();
    }

    public void cargarMedicamentos(){
        medicamentoViewModel.setEstaCargando(true);
        try{
            List<Medicamento> medicamento = medicamentoInteractor.consultarMedicamentos();
            medicamentoViewModel.setListaMedicamentos(medicamento);
            medicamentoViewModel.setMensajeError(null);
        }catch (Exception ex){
            medicamentoViewModel.setMensajeError("Error al cargar medicamentos" + ex.getMessage());
            medicamentoViewModel.setListaMedicamentos(List.of());
        }
        medicamentoViewModel.setEstaCargando(false);
    }

    public void nuevo(){
        medicamentoViewModel.setMedicamentoSeleccionado(new Medicamento());
    }

    public void guardar(){
        Medicamento medicamento = medicamentoViewModel.getMedicamentoSeleccionado();
        boolean resultado;
        String mensaje;

        try{
            if(medicamento.getId_medicamento() == 0){
                resultado = medicamentoInteractor.registrarMedicamento(medicamento);
                mensaje = resultado ? "Medicamento creado con sucesso" : "Error al cargar";
            }else{
                resultado = medicamentoInteractor.actualizarMedicamento(medicamento);
                mensaje = resultado ? "Medicamento actualizado con sucesso" : "Error al cargar";
            }

            if(resultado){
                cargarMedicamentos();
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
        try {
            boolean resultado = medicamentoInteractor.eliminarMedicamento(medicamentoViewModel.getMedicamentoSeleccionado().getId_medicamento());

            if(resultado) {
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Medicamento eliminado");
                cargarMedicamentos();
                nuevo();
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar ");
            }
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_FATAL, "Error grave", ex.getMessage());
        }
    }

    public MedicamentoViewModel getMedicamentoViewModel() {
        return medicamentoViewModel;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
