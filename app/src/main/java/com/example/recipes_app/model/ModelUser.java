package com.example.recipes_app.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.recipes_app.MyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ModelUser {
    public static final ModelUser instance = new ModelUser();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    private ModelFirebase modelFirebase = new ModelFirebase();
    MutableLiveData<UserListLoadingState> userListLoadingState = new MutableLiveData<>();


    public enum UserListLoadingState {
        loaded
    }

    private ModelUser(){
        userListLoadingState.setValue(UserListLoadingState.loaded);
    }

//    public boolean isSignedIn() {
//        return modelFirebase.isSignedIn();
//    }

    public void refreshUserList() {
        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("UserLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllUsers(lastUpdateDate, list -> executor.execute(() -> {
            Long lud = new Long(0);
            Log.d("TAG", "fb returned " + list.size());
            for (User user : list) {
                if(user.getIsConnected().equals("true")) {
                    AppLocalDb.db.userDao().insertAll(user);
                }
                if (lud < user.getUpdateDate()) {
                    lud = user.getUpdateDate();
                }
            }
            MyApplication.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("UserLastUpdateDate", lud)
                    .commit();
        }));
    }

    public FirebaseUser getCurrentUser(){
        return modelFirebase.getCurrentUser();
    }
    public FirebaseAuth getFirebaseAuth(){
        return modelFirebase.getFirebaseAuth();
    }
    public String getCurrentUserFullName(){
        return modelFirebase.getCurrentUser().getDisplayName();
    }
    public String getCurrentUsername(){
        String email = getCurrentUserEmail();
        int index = email.indexOf('@');
        return email.substring(0,index);
    }

    public String getCurrentUserEmail(){
        return modelFirebase.getCurrentUser().getEmail();
    }

    public interface AddUserListener {
        void onComplete();
        void onFailure();
    }

    public interface SigninUserListener{
        void onComplete();
        void onFailure();
    }
    public interface LogoutUserListener{
        void onComplete();

    }

    public interface GetUserByEmail {
        void onComplete(User user);
        void onFailure();
    }

    public interface EditUserListener{
        void onComplete();
    }

    public void editUser(User newUser, EditUserListener listener){
        modelFirebase.editUser(newUser, ()->{
            listener.onComplete();
            refreshUserList();
        });
    }

    public void addUser(User user,String email, String password, AddUserListener listener) {
        modelFirebase.addUser(user, email, password, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
                refreshUserList();
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }
        });
    }

    public void signIn(User user,String email, String password, SigninUserListener listener){
        modelFirebase.signIn(user, email, password, new SigninUserListener() {
            @Override
            public void onComplete() {
                user.setIsConnected("true");
                listener.onComplete();
                refreshUserList();
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }
        });
    }

    public void logout(String currentUserEmail , LogoutUserListener listener){
        modelFirebase.logout(currentUserEmail, () -> listener.onComplete());
    }

    public User getUserByEmail(String email, GetUserByEmail listener) {
        modelFirebase.getUserByEmail(email, listener);
        return null;
    }

    public String getUserId(){
        return modelFirebase.getUserId();
    }



}
