package com.example.recipes_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.recipes_app.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;
    private GoogleSignInClient mGoogleSignInClient;
   // private FirebaseAuth firebaseAuth;

    GoogleApiClient mGoogleApiClient;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //setContentView(R.layout.activity_main);
//        SignInButton authButton = findViewById(R.id.google_signin);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, connectionResult -> { })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
       // firebaseAuth = FirebaseAuth.getInstance();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        authButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });


       // checkUser();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.myAccount_nav, R.id.newRecipeFragment)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            // signed in. Show the "sign out" button and explanation.
//            // ...
//        } else {
//
//        }

//        if(!mGoogleApiClient.isConnected()){
//            hideItem();//TODO!!!!!!!!!!!!!!!!!!
//        }



        /////////new ahu




    }

    private void hideItem()
    {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.newRecipeFragment).setVisible(false);
        nav_Menu.findItem(R.id.myAccount_nav).setVisible(false);


    }

    private void showItem(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.newRecipeFragment).setVisible(true);
        nav_Menu.findItem(R.id.myAccount_nav).setVisible(true);


    }

//
//    private void checkUser() {
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if(firebaseUser != null){
//            //startActivity(new Intent(this,Profile.class));
//            //firebaseAuth.signOut();
//            //mGoogleApiClient.clearDefaultAccountAndReconnect();
//
////            NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
////            navController.navigateUp();
////            navController.navigate(R.id.myAccount_nav);
//            finish();
//        }
//    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item =menu.findItem(R.id.menu_myAccount);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myaccount_menu,menu);
        getMenuInflater().inflate(R.menu.logout_menu,menu);

        //getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.menu_myAccount);
//        item.setVisible(false);

//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.base_menu,menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, 0);
//        mGoogleApiClient.clearDefaultAccountAndReconnect();
//        if(mGoogleApiClient.isConnected()) {//TODO!!!!!!!!!!!!!!!!!!!
//            showItem();
//        }
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
//                    Toast.makeText(MainActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                }
//            } catch (ApiException e) {
//                Log.w("AUTH", "Google sign in failed", e);
//                Toast.makeText(MainActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }

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
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("AUTH", "signInWithCredential:success");
//                             //startActivity(new Intent(MainActivity.this, Profile.class));
//
//                            NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
//                            navController.navigateUp();
//                            navController.navigate(R.id.myAccount_nav);
//                            Toast.makeText(MainActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

}