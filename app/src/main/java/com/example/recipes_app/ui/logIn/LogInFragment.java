package com.example.recipes_app.ui.logIn;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;

public class LogInFragment extends Fragment {

    EditText username;
    EditText password;
    String usernameAsId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        username = view.findViewById(R.id.login_username_tv);
        password = view.findViewById(R.id.lodin_password_tv);
        //recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();


        Button signUp = view.findViewById(R.id.login_signup_btn);
        signUp.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_signUpFragment);
        });

        Button logIn = view.findViewById(R.id.login_login_btn);
        logIn.setOnClickListener((v)->{
            boolean bool = save();
            //Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_categoriesListFragment);
            if(bool) {
                NavHostFragment.findNavController(this).navigate(LogInFragmentDirections.actionNavHomeToCategoriesListFragment2(usernameAsId));
            }
           // Navigation.findNavController(v).navigate(R.id.action_nav_home_to_categoriesListFragment2);
        });


//
//        Model.instance.getUserByUsername(username1, new Model.GetUserByUsername() {
//
//            @Override
//            public void onComplete(User user) {
//
//            }
//        });

        return view;
    }

    private boolean save() {
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        usernameAsId = username1;

        if (TextUtils.isEmpty(username1)){
            username.setError("Please Enter username!");
        }
        else if(TextUtils.isEmpty(password1)){
            password.setError("Please Enter password!");
        }
        else {
            Model.instance.getUserByUsername(username1, new Model.GetUserByUsername() {

                @Override
                public void onComplete(User user) {
                    Log.d("TAG", "hello " + username1);
                    user.setUsername(username1);
                    user.setPassword(password1);
                }
            });
            return true;
        }
        return false;
       // NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));


    }
}