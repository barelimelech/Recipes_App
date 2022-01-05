package com.example.recipes_app.ui.signUp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.example.recipes_app.ui.logIn.UsersListViewModel;

import java.util.List;

public class SignUpFragment extends Fragment {

    EditText username;
    EditText password;
    EditText fullName;
    Button signUp;

    String usernameAsId;

    UsersListViewModel viewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UsersListViewModel.class);
    }


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
            List<User> users = viewModel.getUsers().getValue();
            for(User u : users) {
                if (u.getUsername().equals(username1) && u.getPassword().equals(password1) && u.getFullName().equals(fullName1)){

                    username.setError("User already exist!");
                    return false;
                }
            }
            Model.instance.addUser(user, () -> {
                //Navigation.findNavController(username).navigateUp();

            });
            return true;
        }

        return false;
    }
}