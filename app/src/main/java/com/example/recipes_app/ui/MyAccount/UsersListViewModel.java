package com.example.recipes_app.ui.MyAccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.model.User;
import com.example.recipes_app.ui.RecipesList.RecipesListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UsersListViewModel extends ViewModel {

    LiveData<List<User>> users;
    RecipesListViewModel recipesListViewModel;

    public UsersListViewModel() {
        users = Model.instance.getAllUsers();
        recipesListViewModel = new RecipesListViewModel();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public List<Recipe> getAllUserRecipes(String username){
        List<Recipe> recipes = recipesListViewModel.getRecipes().getValue();
        List<Recipe> tmp = new ArrayList<>();

        for(Recipe r : recipes){
            if(r.getUsername().equals(username)){
                tmp.add(r);
            }
        }
        return  tmp;
    }

}
