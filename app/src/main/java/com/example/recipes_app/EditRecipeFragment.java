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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRecipeFragment extends Fragment {
    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;
    Recipe recipe;
    Button saveRecipe;
    Button backBtn;
    //TextView recipeId;
    String recipeId, usernameAsId;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        recipeId = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipeId();
        //usernameAsId = MyAccountFragmentArgs.fromBundle(getArguments()).getUsername(); //TODO:!!!!!!!
        recipeName = view.findViewById(R.id.editrecipe_name_tv);
        recipeMethod= view.findViewById(R.id.editrecipe_method_tv);
        recipeIngredients= view.findViewById(R.id.editrecipe_ingredients_yv);
        db = FirebaseFirestore.getInstance();
        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
            @Override
            public void onComplete(Recipe student) {
                recipeName.setText(student.getName());
                recipeMethod.setText(student.getMethod());
                recipeIngredients.setText(student.getIngredients());

            }
        });


        saveRecipe = view.findViewById(R.id.editrecipe_save_btn);
        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });



        backBtn = view.findViewById(R.id.editrecipe_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });
        setHasOptionsMenu(true);
        return view;
    }

    //TODO:save..
    private void save() {
        saveRecipe.setEnabled(false);
        backBtn.setEnabled(false);
        String name = recipeName.getText().toString();
        Log.d("TAG", "name: " + name);
        String method = recipeMethod.getText().toString();
        String ingredients = recipeIngredients.getText().toString();
        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
            @Override
            public void onComplete(Recipe student) {
                student.setName(name);
                student.setMethod(method);
                student.setIngredients(ingredients);
            }
        });
//        Recipe updatedRecipe = new Recipe();
//        updatedRecipe.setName(name);
//        Log.d("TAG", "name: " + updatedRecipe.getName());
//        updatedRecipe.setMethod(method);
//        updatedRecipe.setIngredients(ingredients);
//        Map<String, Object> json = updatedRecipe.toJson();
//        db.collection("Recipe.COLLECTION_NAME")
//                .document(recipeId)
//                .set(json);
       // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2());//TODO:!!!!

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteRecipe){
            Log.d("TAG","delete...");
            //TODO:delete
            // Model.instance.deleteRecipeNew(recipe.getId());

            //TODO:
           // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

}