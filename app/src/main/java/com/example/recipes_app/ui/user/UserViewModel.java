package com.example.recipes_app.ui.user;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.auth.User;

public class UserViewModel {
    public LiveData<User> user;
}
