package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes_app.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    TextView name, mail;
    Button logout;

    private ActivityProfileBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_profile);
        setContentView(binding.getRoot());

//        logout = findViewById(R.id.logout);
//        name = findViewById(R.id.name);
//        mail = findViewById(R.id.mail);
//
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount!=null){
//            name.setText(signInAccount.getDisplayName());
//            mail.setText(signInAccount.getEmail());
//        }

        firebaseAuth= FirebaseAuth.getInstance();
        checkUser();


        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Profile.class);
//                startActivity(intent);
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Profile.class);
//                startActivity(intent);
//            }
//        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else{
            String email=firebaseUser.getEmail();
            binding.email.setText(email);
        }
    }

}