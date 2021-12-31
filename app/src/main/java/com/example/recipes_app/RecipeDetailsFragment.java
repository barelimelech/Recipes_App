package com.example.recipes_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

public class RecipeDetailsFragment extends Fragment {
    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;

    TextView recipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe, container, false);

        String recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();
        recipeName = view.findViewById(R.id.recipeDetails_nameOfRec);
        recipeMethod= view.findViewById(R.id.recipeDetails_method);
        recipeIngredients= view.findViewById(R.id.recipeDetails_ingredients);

        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
            @Override
            public void onComplete(Recipe student) {
                recipeName.setText(student.getName());
                recipeMethod.setText(student.getMethod());
                recipeIngredients.setText(student.getIngredients());

            }
        });

        Button editRecipe = view.findViewById(R.id.recipeDetails_edit_btn);
        editRecipe.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_recipeFragment_to_editRecipeFragment);
        });


        Button backBtn = view.findViewById(R.id.recipeDetails_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });
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
}