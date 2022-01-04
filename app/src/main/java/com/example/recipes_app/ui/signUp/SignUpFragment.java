package com.example.recipes_app.ui.signUp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;

public class SignUpFragment extends Fragment {

    EditText username;
    EditText password;
    EditText fullName;
    Button signUp;

    String usernameAsId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        username = view.findViewById(R.id.signup_username_tv);
        password = view.findViewById(R.id.signup_password_tv);
        fullName = view.findViewById(R.id.signup_fullname_tv);

        signUp = view.findViewById(R.id.signup_already_login_btn);
        signUp.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_nav_home);

        });

        Button logIn = view.findViewById(R.id.signup_login_btn);
        logIn.setOnClickListener((v)->{
            boolean bool = save();
            //Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_categoriesListFragment);
            if(bool) {
                NavHostFragment.findNavController(this).navigate(SignUpFragmentDirections.actionSignUpFragmentToCategoriesListFragment(usernameAsId));
            }
        });
        setHasOptionsMenu(true);

        return view;
    }

    private boolean save() {
        signUp.setEnabled(false);
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        String fullName1 = fullName.getText().toString();
        usernameAsId = username1;
        if (TextUtils.isEmpty(username1)){
            username.setError("Please Enter username!");
        }
        else if(TextUtils.isEmpty(password1)){
            password.setError("Please Enter password!");
        }
        else if(TextUtils.isEmpty(fullName1)){
            fullName.setError("Please Enter full name!");
        }
        else {
            User user = new User(username1, password1, fullName1);

            Model.instance.addUser(user, () -> {
                //Navigation.findNavController(username).navigateUp();

            });
            return true;
        }

        return false;
    }
}