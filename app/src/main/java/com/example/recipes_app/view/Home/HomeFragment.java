package com.example.recipes_app.view.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipes_app.R;
import com.example.recipes_app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


    //***********************GOOGLE_SIGN_IN****************************//
//    GoogleApiClient mGoogleApiClient;
//    private GoogleSignInClient mGoogleSignInClient;
//    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //***********************GOOGLE_SIGN_IN****************************//
//        SignInButton authButton = root.findViewById(R.id.google_signin);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mAuth = FirebaseAuth.getInstance();
//        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
//        authButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//
//        checkUser();

        //*****************************************************************//

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myaccount_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount){
            Log.d("TAG","ADD...");
            //  NavHostFragment.findNavController(this).navigate(HomeFragmentDirection.actionGlobalMyAccountFragment());
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }


    //***********************GOOGLE_SIGN_IN****************************//

//    private void checkUser() {
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if(firebaseUser != null){
//            startActivity(new Intent(getActivity(), Profile.class));
//            finish();
//        }
//    }
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
//                    Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                }
//            } catch (ApiException e) {
//                Log.w("AUTH", "Google sign in failed", e);
//                Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }

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
//                            startActivity(new Intent(getActivity(), Profile.class));
//                            Toast.makeText(getActivity(), "Sign-in successful!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getActivity(), "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
}