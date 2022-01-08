package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes_app.databinding.FragmentMyAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    TextView name, mail;
    Button logout;

    private FragmentMyAccountBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMyAccountBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_profile);
        setContentView(binding.getRoot());


        firebaseAuth= FirebaseAuth.getInstance();
        checkUser();


        binding.myAccountLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

//        binding.profileAddrecipeBtn.setOnClickListener(v -> {
//
//        });


    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else{
            String email=firebaseUser.getEmail();
            String userName = firebaseUser.getDisplayName();
            //String familyName = firebaseUser.getF
            binding.myaccountFullnameTv.setText(userName);

        }
    }

}