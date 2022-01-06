//package com.example.recipes_app;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.recipes_app.databinding.ActivityGoogleLogInBinding;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class GoogleLogInActivity extends AppCompatActivity {
//
//    private ActivityGoogleLogInBinding binding;
//
//    private GoogleSignInClient mGoogleSignInClient;
//    private FirebaseAuth mAuth;
//    //private int permission =0;
//
//    GoogleApiClient mGoogleApiClient;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityGoogleLogInBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//
//        SignInButton authButton = findViewById(R.id.google_signin);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mAuth = FirebaseAuth.getInstance();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        authButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//
//
//        checkUser();
//
//    }
//    private void checkUser() {
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if(firebaseUser != null){
//            startActivity(new Intent(this,Profile.class));
//            finish();
//        }
//    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, 0);
//        mGoogleApiClient.clearDefaultAccountAndReconnect();
//
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                if (account != null) {
//                    firebaseAuthWithGoogle(account);
//                } else{
//                    Log.w("AUTH", "Account is NULL");
//                    Toast.makeText(GoogleLogInActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                }
//            } catch (ApiException e) {
//                Log.w("AUTH", "Google sign in failed", e);
//                Toast.makeText(GoogleLogInActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//
//        Log.d("AUTH", "firebaseAuthWithGoogle:" + account.getId());
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("AUTH", "signInWithCredential:success");
//                            startActivity(new Intent(GoogleLogInActivity.this, Profile.class));
//                            Toast.makeText(GoogleLogInActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(GoogleLogInActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//
//
//}