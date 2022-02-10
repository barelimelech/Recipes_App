package com.example.recipes_app.view.MyAccount;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;

import com.example.recipes_app.model.User;

public class UserViewModel extends ViewModel {
    public MutableLiveData<User> user;

    public UserViewModel(){
        user = new MutableLiveData<>();
    }

    public String getCurrentUserEmail(){
        return Model.instance.getCurrentUserEmail();
    }

    public MutableLiveData<User> getUserByEmail(String currentUserEmail, Model.GetUserByEmail listener){
        user.setValue(Model.instance.getUserByEmail(currentUserEmail, listener));
        return user;
    }

    public void updatePassword(String password1){
        Model.instance.getCurrentUser().updatePassword(password1);

    }

    public String getUserId(){
        return Model.instance.getUserId();
    }

    public void editUser(User user1 , Model.EditUserListener listener){
        user.setValue(user1);
        Model.instance.editUser(user1,listener);
    }
    public void saveImage(Bitmap img, String name, Model.SaveImageListener listener){
        Model.instance.saveImage(img,name,listener);
    }

    public String getCurrentUser(){
        return Model.instance.getCurrentUsername();
    }
    public String getCurrentUserFullName(){
        return Model.instance.getCurrentUserFullName();
    }
    public String getCurrentUserUsername(){
        return Model.instance.getCurrentUsername();
    }

    public void signOut(){
        Model.instance.getFirebaseAuth().signOut();
    }

    public void logout(String email , Model.LogoutUserListener listener){
        Model.instance.logout(email, listener);
    }

    public void signIn(User user,String email,String password, Model.SigninUserListener listener){
        Model.instance.signIn(user,email,password,listener);
    }

    public void addUser(User user,String email,String password, Model.AddUserListener listener){
        Model.instance.addUser(user, email, password,listener);
    }
}
