package com.example.recipes_app.ui.signUp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;

public class SignUpFragment extends Fragment {

    EditText username;
    EditText password;
    EditText birthday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        username = view.findViewById(R.id.login_username_tv);
        password = view.findViewById(R.id.lodin_password_tv);
        birthday = view.findViewById(R.id.signup_birthday_tv);

        Button signUp = view.findViewById(R.id.signup_already_login_btn);
        signUp.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_nav_home);
        });

        Button logIn = view.findViewById(R.id.signup_login_btn);
        logIn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_categoriesListFragment);
        });

        return view;
    }
}