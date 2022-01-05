package com.example.recipes_app;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends Fragment {
    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;
    TextView type;
    Button deleteRecipe;
    //TextView recipeId;
    String recipeNameAsId;

    ImageView recipeImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe, container, false);

        recipeNameAsId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();
        recipeName = view.findViewById(R.id.recipeDetails_nameOfRec);
        recipeMethod= view.findViewById(R.id.recipeDetails_method);
        recipeIngredients= view.findViewById(R.id.recipeDetails_ingredients);
        type= view.findViewById(R.id.recipeDetails_type);

        recipeImage = view.findViewById(R.id.newRec_image_recipe);

        Model.instance.getRecipeByRecipeName(recipeNameAsId, new Model.GetRecipeByRecipeName() {
            @Override
            public void onComplete(Recipe recipe) {
                recipeName.setText(recipe.getName());
                recipeMethod.setText(recipe.getMethod());
                recipeIngredients.setText(recipe.getIngredients());
                type.setText(recipe.getType());
                if(recipe.getRecipeUrl()!=null){
                    Picasso.get().load(recipe.getRecipeUrl()).into(recipeImage);
                }

            }
        });

        Button editRecipe = view.findViewById(R.id.recipeDetails_edit_btn);
        editRecipe.setOnClickListener((v)->{
            //Navigation.findNavController(v).navigate(R.id.action_recipeFragment_to_editRecipeFragment);
            NavHostFragment.findNavController(this).navigate(RecipeDetailsFragmentDirections.actionRecipeFragmentToEditRecipeFragment(recipeNameAsId));
        });

        deleteRecipe = view.findViewById(R.id.recipeDetails_delete_btn);
        deleteRecipe.setOnClickListener((v)->{
            delete();
        });


        Button backBtn = view.findViewById(R.id.recipeDetails_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });
        setHasOptionsMenu(true);
        return view;
//
//        String stId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();
//        Recipe recipe = Model.instance.getRecipeById(stId);
//
//        TextView recipeName = view.findViewById(R.id.recipe_listrow_name);
//
//        recipeName.setText(recipe.getName());
//
////        Button backBtn = view.findViewById(R.id.details_back_btn);
////        backBtn.setOnClickListener((v)->{
////            Navigation.findNavController(v).navigateUp();
////        });
//        return view;
    }

    private void delete() {
        deleteRecipe.setEnabled(false);

        Model.instance.deleteRecipe(recipeNameAsId,()->{

        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detalis_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.EditRecipeFragment){
            Log.d("TAG","edit...");
            NavHostFragment.findNavController(this).navigate(RecipeDetailsFragmentDirections.actionRecipeFragmentToEditRecipeFragment(recipeNameAsId));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}