package com.example.recipes_app.view.MyAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.model.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAccountFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    List<Recipe> recipes;
    String usernameAsId;
    TextView fullName;
    Button newRecipe;
    Button logOutBtn;
    View view;
    String fullNameAsId;
    GoogleApiClient mGoogleApiClient;
    String currentUserEmail;
    ImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_account, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userImage = view.findViewById(R.id.myAccount_image_user);

        Model.instance.getUserByEmail(Model.instance.getCurrentUserEmail(), new Model.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
                if(user.getUserUrl()!=null) {
                    Picasso.get().load(user.getUserUrl()).into(userImage);
                }
            }

            @Override
            public void onFailure() {

            }
        });


        //usernameAsId = MyAccountFragmentArgs.fromBundle(getArguments()).getUsername();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.toString();
        } else {

        }
        fullName = view.findViewById(R.id.myaccount_fullname_tv);
        checkUser();

        newRecipe = view.findViewById(R.id.myaccount_addrecipe_btn);
        newRecipe.setOnClickListener((v)->{
             //Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionGlobalNewRecipeFragment(Model.instance.getCurrentUsername()));

        });

        Button myRecipes = view.findViewById(R.id.myaccount_myrecipes_btn);
        myRecipes.setOnClickListener((v)->{
           // Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment(Model.instance.getCurrentUsername(), ""));

        });
        Button othersRecipes = view.findViewById(R.id.myaccount_othersrecipes_btn);
        othersRecipes.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment("",""));

           // Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);//TODO
        });

        Button editMyAccount = view.findViewById(R.id.myaccount_edit_btn);
        editMyAccount.setOnClickListener((v)->{
            //Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_editMyAccountFragment);
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToEditMyAccountFragment(fullNameAsId));

        });

        Button categories = view.findViewById(R.id.myaccount_categories_btn);
        categories.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToCategoriesListFragment(fullNameAsId));
        });


        logOutBtn = view.findViewById(R.id.myAccount_logout_btn);
        logOutBtn.setOnClickListener((v)->{
            currentUserEmail = Model.instance.getCurrentUserEmail();
            Model.instance.getFirebaseAuth().signOut();
            checkUser();
        });
        //TODO: edit my account page and fragment
//        Button editMyAccount = view.findViewById(R.id.myaccount_edit_btn);
//        editMyAccount.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
//        });

//        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
//
//            @Override
//            public void onComplete(User user) {
//                fullName.setText(user.getFullName());
//            }
//        });



        setHasOptionsMenu(true);

        return view;


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item =menu.findItem(R.id.menu_myAccount);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = Model.instance.getCurrentUserEmail();
            Model.instance.getFirebaseAuth().signOut();
            Model.instance.logout(currentUserEmail, new Model.LogoutUserListener() {
                @Override
                public void onComplete() {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });

            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void checkUser() {
        //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(Model.instance.getCurrentUser() == null){
            //NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToNavHome());

            //Navigation.findNavController(view).navigate(R.id.nav_host_fragment_content_main);
           // getActivity().finish();
             Model.instance.logout(currentUserEmail, new Model.LogoutUserListener() {
                 @Override
                 public void onComplete() {
                     startActivity(new Intent(getActivity(), LoginActivity.class));
                     getActivity().finish();
                 }
             });

        }else{
            String email=Model.instance.getCurrentUserEmail();
            String userName = Model.instance.getCurrentUserFullName();
            if (userName == null || userName.equals("")) {
                userName = Model.instance.getCurrentUsername();
            }
            fullName.setText(userName);
            fullNameAsId = userName;

        }
    }



}