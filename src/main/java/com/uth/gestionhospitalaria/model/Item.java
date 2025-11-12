package com.uth.gestionhospitalaria.model;

import java.io.Serializable;

public class Item implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;

    public Item() {}

    public Item(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}