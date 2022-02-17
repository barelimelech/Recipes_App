package com.example.recipes_app.view.login;

import static android.graphics.Color.rgb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.recipes_app.MainActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.User;
import com.example.recipes_app.view.MyAccount.UserViewModel;


public class LoginFragment extends Fragment {

    EditText emailTv,passwordTv;
    Button signUpBtn,loginBtn;

    ProgressBar progressBar;

    UserViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailTv = view.findViewById(R.id.login_email_et);
        passwordTv = view.findViewById(R.id.login_password_et);

        progressBar = view.findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(255, 204, 204), android.graphics.PorterDuff.Mode.MULTIPLY);

        signUpBtn =view.findViewById(R.id.login_signup_btnnnn);
        loginBtn =view.findViewById(R.id.login_login_btnnnn);
        loginBtn.setOnClickListener(v -> {
            String email = emailTv.getText().toString().toLowerCase();
            String password = passwordTv.getText().toString();
            boolean b = check();
            if(b) {
                viewModel.getUserByEmail(email, new ModelUser.GetUserByEmail() {
                    @Override
                    public void onComplete(User user) {
                        User newUser = user;
                        newUser.setIsConnected("true");

                        viewModel.editUser(newUser, () -> {
                            boolean bool = save();
                            if (bool == true) {
                                viewModel.signIn(user, email, password, new ModelUser.SigninUserListener(){
                                    @Override
                                    public void onComplete() { toFeedActivity(); }
                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(getActivity(), "Email or password is not correct.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "Email or password is not correct.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
           }

        });

        signUpBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment()));
        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private boolean check(){
        String email = emailTv.getText().toString().toLowerCase();
        String password = passwordTv.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailTv.setError("Please Enter email :)");
            return false;

        }else if(!email.contains("@")){
            emailTv.setError("Email must contain '@' :)");
            return false;

        }else if(TextUtils.isEmpty(password)){
            passwordTv.setError("Please Enter password :)");

            return false;
        }
        return true;
    }
    private boolean save() {

        progressBar.setVisibility(View.VISIBLE);
        String email = emailTv.getText().toString().toLowerCase();
        String password = passwordTv.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailTv.setError("Please Enter email :)");


        }else if(TextUtils.isEmpty(password)){
            passwordTv.setError("Please Enter password :)");

        }
        else {
            return true;
        }
        return false;

    }
}