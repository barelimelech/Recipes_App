package com.example.recipes_app.ui.logIn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;

public class LogInFragment extends Fragment {

    EditText username;
    EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        username = view.findViewById(R.id.login_username_tv);
        password = view.findViewById(R.id.lodin_password_tv);


        Button signUp = view.findViewById(R.id.login_signup_btn);
        signUp.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_signUpFragment);
        });

        Button logIn = view.findViewById(R.id.login_login_btn);
        logIn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_categoriesListFragment2);
        });
        return view;
    }
//
//    private void save() {
//        //progressBar.setVisibility(View.VISIBLE);
//        saveBtn.setEnabled(false);
//        //cancelBtn.setEnabled(false);
//        String name = recipeName.getText().toString();
//        String method = recipeMethod.getText().toString();
//        String ingredients = recipeIngredients.getText().toString();
//        String id = recipeId.getText().toString();
//        if (TextUtils.isEmpty(name)){
//            nameEt.setError("Please Enter name!");
//        }
//        else if(TextUtils.isEmpty(id)){
//            idEt.setError("Please Enter id!");
//        }
//        Recipe recipe = new Recipe(name,method,ingredients,id);
//        Model.instance.addRecipe(recipe,()->{
//            Navigation.findNavController(recipeId).navigateUp();
//        });
//
//    }
}