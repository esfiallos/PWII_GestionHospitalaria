package com.uth.gestionhospitalaria.data;

import java.util.List;

public class OrdsResponse<T> {
    // El nombre "items" debe coincidir EXACTO con el JSON
    private List<T> items;

    // Constructor vacío
    public OrdsResponse() {
    }

    // Getter y Setter
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
