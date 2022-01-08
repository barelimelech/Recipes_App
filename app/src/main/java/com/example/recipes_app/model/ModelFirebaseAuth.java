//package com.example.recipes_app.model;
//
//import android.util.Log;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class ModelFirebaseAuth {
//
//    private FirebaseAuth firebaseAuth;
//
//    public ModelFirebaseAuth(){
//        firebaseAuth = FirebaseAuth.getInstance();
//    }
//
//    public FirebaseUser getCurrentUser(){
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        return firebaseUser;
//    }
//
//    public void firebaseAuthWithGoogle(GoogleSignInAccount account, Model.ConnectWithGoogle listener) {
//
//        Log.d("AUTH", "firebaseAuthWithGoogle:" + account.getId());
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(task -> {
//                    listener.onComplete();
////                        if (task.isSuccessful()) {
////
////                            Log.d("AUTH", "signInWithCredential:success");
////                            //startActivity(new Intent(MainActivity.this, Profile.class));
////
//////                            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
//////                            navController.navigateUp();
//////                            navController.navigate(R.id.myAccount_nav);
//////                            Toast.makeText(MainActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
////                        } else {
////                            //Log.w("AUTH", "signInWithCredential:failure", task.getException());
////                            Toast.makeText(MainActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
////                        }
//                });
//    }
//
//}
