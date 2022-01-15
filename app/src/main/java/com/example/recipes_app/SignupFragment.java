package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignupFragment extends Fragment {
    EditText emailTv;
    EditText passwordTv;
    Button signUpBtn;
    Button loginBtn;
    EditText fullName;
    EditText phone;
    private GoogleSignInClient mGoogleSignInClient;

    GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailTv = view.findViewById(R.id.signup22_email_tv);
        passwordTv = view.findViewById(R.id.signup22_password_tv);

        signUpBtn =view.findViewById(R.id.signup22_signup_btn);
       // loginBtn =view.findViewById(R.id.signup22_login_btn);
        fullName = view.findViewById(R.id.signup22_fullname_tv);
        phone = view.findViewById(R.id.signup22_phone_tv);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//
//            }
//        });



        return view;
    }


    private void save() {
        //signUpBtn.setEnabled(false);
        String email = emailTv.getText().toString();
        String password = passwordTv.getText().toString();
        String phone1 = phone.getText().toString();
        String fullName2 = fullName.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailTv.setError("Please Enter email :)");

        }else if(TextUtils.isEmpty(password)){
            passwordTv.setError("Please Enter password :)");

        }else if(TextUtils.isEmpty(phone1)){
            phone.setError("Please Enter phone number :)");

        }else if(TextUtils.isEmpty(fullName2)){
            fullName.setError("Please Enter full name :)");

        }
        else {

            User user = new User(fullName2,phone1,email,"0");
            Model.instance.addUser(user,email,password ,() -> {
                //Navigation.findNavController(v).navigate(R.id.);
                startActivity(new Intent(getActivity(), LoginActivity.class));

            });

        }

    }
}