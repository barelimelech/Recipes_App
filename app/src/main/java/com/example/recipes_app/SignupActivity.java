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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

        EditText emailTv;
        EditText passwordTv;
    Button signUpBtn;
    Button loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailTv = this.findViewById(R.id.signup2_email_tv);
        passwordTv = findViewById(R.id.signup2_password_tv);

        signUpBtn =findViewById(R.id.signup2_signup_btn);
        loginBtn =findViewById(R.id.signup2_login_btn);
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                createAccount(email,password);
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
//    private FirebaseAuth mAuth;
//    EditText emailTv,passwordTv;
//    Button signUpBtn,loginBtn;
//    SignupActivity binding;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        //binding = SignupActivity.inflate(getLayoutInflater());
//        //setContentView(binding.getRoot());
//
//        mAuth = FirebaseAuth.getInstance();
//        emailTv = this.findViewById(R.id.signup2_email_tv);
//        passwordTv = findViewById(R.id.signup2_password_tv);
//
//        signUpBtn =findViewById(R.id.signup2_signup_btn);
//        loginBtn =findViewById(R.id.signup2_login_btn);
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailTv.getText().toString();
//                String password = passwordTv.getText().toString();
//
//                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
//                    Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
//
//                }else{
//                    createUser(email,password);
//                }
//            }
//        });
//
//
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email1 = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
//        }
//
//
//
//    }
//
//    private void createUser(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
//
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
////                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(SignupActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//
//    }
//
//    private void updateUI(@NonNull FirebaseUser user) {
//        // No-op
//    }
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//           // reload();
//        }
//    }

}