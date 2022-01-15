//package com.example.recipes_app;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.recipes_app.model.Model;
//import com.example.recipes_app.model.User;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
//public class SignupActivity extends AppCompatActivity {
//
//    private static final String TAG = "EmailPassword";
//    // [START declare_auth]
//    private FirebaseAuth mAuth;
//    // [END declare_auth]
//
//        EditText emailTv;
//        EditText passwordTv;
//    Button signUpBtn;
//    Button loginBtn;
//    EditText fullName;
//    EditText phone;
//
//
//    private GoogleSignInClient mGoogleSignInClient;
//
//    GoogleApiClient mGoogleApiClient;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        emailTv = this.findViewById(R.id.signup22_email_tv);
//        passwordTv = findViewById(R.id.signup22_password_tv);
//
//        signUpBtn =findViewById(R.id.signup22_signup_btn);
//        //loginBtn =findViewById(R.id.signup22_login_btn);
//        fullName = findViewById(R.id.signup22_fullname_tv);
//        phone = findViewById(R.id.signup22_phone_tv);
//        mAuth = FirebaseAuth.getInstance();
//        // [END initialize_auth]
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailTv.getText().toString();
//                String password = passwordTv.getText().toString();
//                String phone1 = phone.getText().toString();
//                String fullName2 = fullName.getText().toString();
//                //createAccount(email,password);
//                User user = new User(fullName2,phone1,email,"0");
//                Model.instance.addUser(user,email,password ,() -> {
//                    //Navigation.findNavController(v).navigate(R.id.);
//                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
//
//                });
//            }
//        });
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//
//            }
//        });
//
//
//
//        //*****************************************************************//
//        SignInButton authButton = findViewById(R.id.google_signin2);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, connectionResult -> { })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        authButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//
//
//    }
//
//    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
//    }
//    // [END on_start_check_user]
//
////    private void createAccount(String email, String password) {
////        // [START create_user_with_email]
////        Model.instance.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
////                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
////                        if (task.isSuccessful()) {
////                            // Sign in success, update UI with the signed-in user's information
////                            Log.d(TAG, "createUserWithEmail:success");
////                            FirebaseUser user = mAuth.getCurrentUser();
////                            String userId = getUId();
////                            updateUI(userId);
////
////                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
////
////                        } else {
////                            // If sign in fails, display a message to the user.
////                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
////                            Toast.makeText(SignupActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
////                        }
////                    }
////                });
////        // [END create_user_with_email]
////    }
//
////    private void signIn(String email, String password) {
////        // [START sign_in_with_email]
////        mAuth.signInWithEmailAndPassword(email, password)
////                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
////                        if (task.isSuccessful()) {
////                            // Sign in success, update UI with the signed-in user's information
////                            Log.d(TAG, "signInWithEmail:success");
////                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
////                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
////
////                        } else {
////                            // If sign in fails, display a message to the user.
////                            Log.w(TAG, "signInWithEmail:failure", task.getException());
////                            Toast.makeText(SignupActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
////                        }
////                    }
////                });
////        // [END sign_in_with_email]
////    }
//
//    private void sendEmailVerification() {
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Email sent
//                    }
//                });
//        // [END send_email_verification]
//    }
//
//    private void reload() { }
//
//    private void updateUI(String userId) {
//        //Model.instance.getUserBId();
//    }
//    private String getUId() {
//        return Model.instance.getUserId();
//    }
//
//    //**************************************************************//
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, 0);
//        mGoogleApiClient.clearDefaultAccountAndReconnect();
////    if(mGoogleApiClient.isConnected()) {
////        showItem();
////    }
//
//    }
//
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
//                    Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                }
//            } catch (ApiException e) {
//                Log.w("AUTH", "Google sign in failed", e);
//                Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
////        Model.instance.connectWithGoogle(account, new Model.ConnectWithGoogle() {
////            @Override
////            public void onComplete() {
////                NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
////                navController.navigateUp();
////                navController.navigate(R.id.myAccount_nav);
////                Toast.makeText(MainActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
////            }
////        });
//        Log.d("AUTH", "firebaseAuthWithGoogle:" + account.getId());
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        Model.instance.getFirebaseAuth().signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("AUTH", "signInWithCredential:success");
//                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
//
////                            NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
////                            navController.navigateUp();
////                            navController.navigate(R.id.myAccount_nav);
//                            Toast.makeText(SignupActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//
//
//}
//
