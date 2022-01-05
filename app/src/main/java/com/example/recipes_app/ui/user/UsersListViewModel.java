package com.example.recipes_app.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;

import java.util.List;

public class UsersListViewModel extends ViewModel {

    LiveData<List<User>> users;

    public UsersListViewModel() {
        users = Model.instance.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

}
