package com.uth.gestionhospitalaria.model;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private static Items instance = null;

    private final List<Item> items;

    private Items() {
        this.items = new ArrayList<>();
    }

    public static Items getInstance() {
        if (instance == null) {
            instance = new Items();
        }
        return instance;
    }

    public List<Item> getItems() {
        return items;
    }
}
