package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes_app.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    EditText emailTv,passwordTv;
    Button signUpBtn,loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_log_in);
        emailTv = this.findViewById(R.id.login2_email_tv);
        passwordTv = findViewById(R.id.login2_password_tv);

        signUpBtn =findViewById(R.id.login2_signup_btn);
        loginBtn =findViewById(R.id.login2_login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                signIn(email,password);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        Model.instance.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                           // FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "User is not exist, please signup first.", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {

    }

}