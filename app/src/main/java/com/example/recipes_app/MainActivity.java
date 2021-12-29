package com.example.recipes_app;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    NavController navCtl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.myAccount_nav, R.id.newRecipeFragment, R.id.searchFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (!super.onOptionsItemSelected(item)){
//            switch (item.getItemId()){
//                case R.id.menu_user:
//                    navCtl.navigate(R.id.action_global_myAccountFragment);
//                    break;
//                case R.id.menu_add:
//                    navCtl.navigate(R.id.action_global_newRecipeFragment);
//                    break;
//                case R.id.menu_search:
//                    navCtl.navigate(R.id.action_global_searchFragment);
//                    break;
//
//                case android.R.id.home:
//                    navCtl.navigateUp();
//                    return true;
//                default:
//                    NavigationUI.onNavDestinationSelected(item,navCtl);
//            }
//        }else{
//            return true;
//        }
//        return false;
//    }
}