package com.example.recipes_app.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model() {




    }

    List<String> data = new LinkedList<String>();

    public List<String> getAllCategories() {
        data.add("Deserts");
        data.add("Breakfast");
        data.add("example");
        data.add("Lunch");
        data.add("Dinner");
        data.add("Holidays");
        return data;
    }
}
