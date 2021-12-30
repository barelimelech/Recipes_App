package com.example.recipes_app.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class MyAccountFragment extends Fragment {
    List<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_account, container, false);


        Button myRecipes = view.findViewById(R.id.myaccount_myrecipes_btn);
        myRecipes.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);
        });
        Button othersRecipes = view.findViewById(R.id.myaccount_othersrecipes_btn);
        othersRecipes.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_myAccount_nav_to_recipesListFragment);
        });

        //TODO: edit my account page and fragment
//        Button editMyAccount = view.findViewById(R.id.myaccount_edit_btn);
//        editMyAccount.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
//        });

        return view;


    }



}