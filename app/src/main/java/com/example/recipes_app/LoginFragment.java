package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.model.Model;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment {

    EditText emailTv,passwordTv;
    Button signUpBtn,loginBtn;


    private GoogleSignInClient mGoogleSignInClient;

    GoogleApiClient mGoogleApiClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailTv = view.findViewById(R.id.login_email_et);
        passwordTv = view.findViewById(R.id.login_password_et);

        signUpBtn =view.findViewById(R.id.login_signup_btnnnn);
        loginBtn =view.findViewById(R.id.login_login_btnnnn);
        loginBtn.setOnClickListener(v -> {
            //TODO - connect to model login function
            String email = emailTv.getText().toString();
            String password = passwordTv.getText().toString();
            boolean bool = save();
            if(bool == true) {
                Model.instance.signIn(email, password, new Model.SigninUserListener() {
                    @Override
                    public void onComplete() {
                        toFeedActivity();
                    }
                });
            }

            //            String uId = Model.instance.getUserId();
//
//            User tmp = Model.instance.getUserBId(uId, new Model.GetUserById() {
//                @Override
//                public void onComplete(User user) {
//                    String email = emailTv.getText().toString();
//                    String password = passwordTv.getText().toString();
//                    Model.instance.signIn(user,email, password, new Model.SigninUserListener() {
//                        @Override
//                        public void onComplete() {
//
//                            toFeedActivity();
//
//                        }
//                    });
//                }
//            });
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment());
            }
        });


        SignInButton authButton = view.findViewById(R.id.google_signin2);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), connectionResult -> {
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
//    if(mGoogleApiClient.isConnected()) {
//        showItem();
//    }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    Log.w("AUTH", "Account is NULL");
                    Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.w("AUTH", "Google sign in failed", e);
                Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Model.instance.connectWithGoogle(account, new Model.ConnectWithGoogle() {
//            @Override
//            public void onComplete() {
//                NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
//                navController.navigateUp();
//                navController.navigate(R.id.myAccount_nav);
//                Toast.makeText(MainActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
//            }
//        });
        Log.d("AUTH", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Model.instance.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "signInWithCredential:success");
                            startActivity(new Intent(getActivity(), MainActivity.class));

//                            NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
//                            navController.navigateUp();
//                            navController.navigate(R.id.myAccount_nav);
                            Toast.makeText(getActivity(), "Sign-in successful!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




    private boolean save() {
        //signUpBtn.setEnabled(false);
        String email = emailTv.getText().toString();
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