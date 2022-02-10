package com.example.recipes_app.view.Categories;

import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;

import java.util.List;

public class CategoriesListViewModel extends ViewModel {
    List<String> categories;

    public CategoriesListViewModel(){
        categories = Model.instance.getAllCategories();
    }

    public List<String> getAllCategories(){
        return categories;
    }
}
