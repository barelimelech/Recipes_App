package com.example.recipes_app.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAccountFragment extends Fragment {
    List<Recipe> recipes;
    String usernameAsId;
    TextView fullName;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_account, container, false);
        usernameAsId = MyAccountFragmentArgs.fromBundle(getArguments()).getUsername();
        db = FirebaseFirestore.getInstance();

        fullName = view.findViewById(R.id.myaccount_fullname_tv);
        Button myRecipes = view.findViewById(R.id.myaccount_myrecipes_btn);
        myRecipes.setOnClickListener((v)->{
           // Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment(usernameAsId,""));

        });
        Button othersRecipes = view.findViewById(R.id.myaccount_othersrecipes_btn);
        othersRecipes.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment(usernameAsId,""));

           // Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);//TODO
        });

        Button edit = view.findViewById(R.id.myaccount_edit_btn);
        edit.setOnClickListener((v)->{
            //Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_editMyAccountFragment);
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToEditMyAccountFragment(usernameAsId));

        });

        Button categories = view.findViewById(R.id.myaccount_categories_btn);
        categories.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToCategoriesListFragment(usernameAsId));
        });
        //TODO: edit my account page and fragment
//        Button editMyAccount = view.findViewById(R.id.myaccount_edit_btn);
//        editMyAccount.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
//        });

        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {

            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
            }
        });



        setHasOptionsMenu(true);

        return view;


    }



}