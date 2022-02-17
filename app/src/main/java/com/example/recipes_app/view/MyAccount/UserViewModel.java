package com.example.recipes_app.view.MyAccount;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.User;

public class UserViewModel extends ViewModel {
    public MutableLiveData<User> user;

    public UserViewModel(){
        user = new MutableLiveData<>();
    }

    public String getCurrentUserEmail(){
        return ModelUser.instance.getCurrentUserEmail();
    }

    public MutableLiveData<User> getUserByEmail(String currentUserEmail, ModelUser.GetUserByEmail listener){
        user.setValue(ModelUser.instance.getUserByEmail(currentUserEmail, listener));
        return user;
    }

    public void updatePassword(String password1){
        ModelUser.instance.getCurrentUser().updatePassword(password1);
    }

    public String getUserId(){
        return ModelUser.instance.getUserId();
    }

    public void editUser(User user1 , ModelUser.EditUserListener listener){
        user.setValue(user1);
        ModelUser.instance.editUser(user1,listener);
    }
    public void saveImage(Bitmap img, String name, ModelRecipe.SaveImageListener listener){
        ModelRecipe.instance.saveImage(img,name,listener);
    }

    public String getCurrentUser(){
        return ModelUser.instance.getCurrentUsername();
    }
    public String getCurrentUserFullName(){
        return ModelUser.instance.getCurrentUserFullName();
    }
    public String getCurrentUserUsername(){
        return ModelUser.instance.getCurrentUsername();
    }

    public void signOut(){
        ModelUser.instance.getFirebaseAuth().signOut();
    }

    public void logout(String email , ModelUser.LogoutUserListener listener){
        ModelUser.instance.logout(email, listener);
    }

    public void signIn(User user,String email,String password, ModelUser.SigninUserListener listener){
        ModelUser.instance.signIn(user,email,password,listener);
    }

    public void addUser(User user,String email,String password, ModelUser.AddUserListener listener){
        ModelUser.instance.addUser(user, email, password,listener);
    }
}
