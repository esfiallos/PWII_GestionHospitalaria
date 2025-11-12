package com.uth.gestionhospitalaria.bean;

import com.uth.gestionhospitalaria.model.Item;
import com.uth.gestionhospitalaria.model.Items;
import jakarta.annotation.PostConstruct; // Importante
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ItemBeans implements Serializable {

    private String filter;
    private List<Item> items;
    private Item selected;

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public ItemBeans() {
        // El constructor debe estar (casi) vacío
    }

    @PostConstruct
    public void init() {
        loadItems();
        selected = new Item();
    }

    private void loadItems() {
        this.items = Items.getInstance().getItems();
    }


    public void filterAction() {
        if (filter == null || filter.trim().isEmpty()) {
            loadItems();
        } else {
            items = Items.getInstance().getItems().stream()
                    .filter(i -> i.getNombre() != null &&
                            i.getNombre().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    public void save() {
        // Item nuevo → ID es null
        if (selected.getId() == null) {
            selected.setId(ID_GENERATOR.getAndIncrement());
            Items.getInstance().getItems().add(selected);
            showInfo("Registro", "Item registrado correctamente");
        } else {
            // Item existente → solo actualiza
            showInfo("Actualización", "Item actualizado correctamente");
        }

        loadItems();
        selected = new Item();
    }


    public void delete(Item itemToDelete) {
        Items.getInstance().getItems().remove(itemToDelete);
        loadItems(); // Recargar la lista
        showInfo("Eliminado", "Item eliminado");
    }

    public void prepareEdit(Item itemToEdit) {
        // Asigna el item de la tabla a la variable 'selected'
        // para que el diálogo muestre sus datos
        this.selected = itemToEdit;
    }

    // Este método ya no es necesario, su lógica está en save()
    // public void registrarItem() { ... }

    // Este método ya no es necesario, su lógica está en filterAction()
    // public List<Item> obtenerItemsFiltrados(String filtro) { ... }

    // --- Métodos de Mensajes (Están bien) ---
    public void showInfo(String messageHeader, String messageDetail) {
        addMessage(FacesMessage.SEVERITY_INFO, messageHeader, messageDetail);
    }

    public void showError(String messageHeader, String messageDetail) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageHeader, messageDetail);
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // --- ARREGLO 5: Getters y Setters para las propiedades que faltaban ---

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getSelected() {
        return selected;
    }

    public void setSelected(Item selected) {
        this.selected = selected;
    }

    // El getter/setter para 'item' original ya no es necesario
}