package com.example.recipes_app.view.RecipeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.view.MyAccount.UserViewModel;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends Fragment {
    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;
    TextView type;
    TextView userName;
    String usernameAsId;
    String recipeNameAsId;

    RecipeDetailsViewModel viewModel;
    UserViewModel userViewModel;

    ImageView recipeImage;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipeDetailsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        usernameAsId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getUsername();
        recipeNameAsId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();
        recipeName = view.findViewById(R.id.recipeDetails_nameOfRec);
        recipeMethod = view.findViewById(R.id.recipeDetails_method);
        recipeIngredients = view.findViewById(R.id.recipeDetails_ingredients);
        type = view.findViewById(R.id.recipeDetails_type);
        userName = view.findViewById(R.id.recipeDetails_username_tv);
        userName.setText(usernameAsId);
        recipeImage = view.findViewById(R.id.editmyaccount_image_recipe);

        viewModel.getRecipeByRecipeName(recipeNameAsId, recipe -> {
            recipeName.setText(recipe.getName());
            recipeMethod.setText(recipe.getMethod());
            recipeIngredients.setText(recipe.getIngredients());
            type.setText(recipe.getType());
            if (recipe.getRecipeUrl() != null) {
                Picasso.get().load(recipe.getRecipeUrl()).into(recipeImage);
            }
            userName.setText(recipe.getUsername());
        });
        Button backBtn = view.findViewById(R.id.recipeDetails_back_btn);
        backBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigateUp();
            viewModel.refreshRecipesList();

        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.myaccount_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            NavHostFragment.findNavController(this).navigate(RecipeDetailsFragmentDirections.actionGlobalMyAccountFragment(userViewModel.getCurrentUser()));
            return true;
        } else if (item.getItemId() == R.id.logout_menu) {
            String currentUserEmail = userViewModel.getCurrentUserEmail();
            userViewModel.signOut();
            userViewModel.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}