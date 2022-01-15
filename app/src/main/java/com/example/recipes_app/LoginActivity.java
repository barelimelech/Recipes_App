package com.example.recipes_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

public class LoginActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    EditText emailTv, passwordTv;
    Button signUpBtn, loginBtn;

    NavController navCtl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_log_in);


        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navCtl = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navCtl);


//        emailTv = this.findViewById(R.id.login2_email_tv);
//        passwordTv = findViewById(R.id.login2_password_tv);
//
//        signUpBtn =findViewById(R.id.login2_signup_btn);
//        loginBtn =findViewById(R.id.login2_login_btn);

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailTv.getText().toString();
//                String password = passwordTv.getText().toString();
//                signIn(email,password);
//            }
//        });
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//            }
//        });



    }





//    private void signIn(String email, String password) {
//        // [START sign_in_with_email]
//        Model.instance.getFirebaseAuth().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "signInWithEmail:success");
//                           // FirebaseUser user = mAuth.getCurrentUser();
//                           // updateUI(user);
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "User is not exist, please signup first.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//        // [END sign_in_with_email]
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//       // getMenuInflater().inflate(R.menu.login_menu,menu);
//        getMenuInflater().inflate(R.menu.myaccount_menu,menu);
//
//        return true;
//    }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            if (!super.onOptionsItemSelected(item)) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        navCtl.navigateUp();
                        return true;
                    default:
                        NavigationUI.onNavDestinationSelected(item, navCtl);
                }
            } else {
                return true;
            }
            return false;
        }

    }
