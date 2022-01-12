package com.example.recipes_app.ui.EditRecipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class EditRecipeFragment extends Fragment {
    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;
    TextView userName;
    Recipe recipe;
    Button saveRecipe;
    Button backBtn;
    Button deleteRecipe;
    String recipeNameAsId, usernameAsId, category;
    Spinner categoriesSpinner;
    List<String> categories = Model.instance.getAllCategories();
    String selectedCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        recipeNameAsId = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipeId();
        usernameAsId = EditRecipeFragmentArgs.fromBundle(getArguments()).getUsername();
        recipeName = view.findViewById(R.id.editrecipe_name_tv);
        recipeMethod= view.findViewById(R.id.editrecipe_method_tv);
        recipeIngredients= view.findViewById(R.id.editrecipe_ingredients_yv);
        categoriesSpinner= view.findViewById(R.id.editrecipe_spinner);
        userName = view.findViewById(R.id.editrecipe_username_tv2);
        userName.setText(usernameAsId);


        recipe = new Recipe();
        Model.instance.getRecipeByRecipeName(recipeNameAsId, new Model.GetRecipeByRecipeName() {
            @Override
            public void onComplete(Recipe student) {
                recipeName.setText(student.getName());
                recipeMethod.setText(student.getMethod());
                recipeIngredients.setText(student.getIngredients());
                userName.setText(student.getUsername());

            }
        });

        initSpinnerFooter();

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

        deleteRecipe = view.findViewById(R.id.editrecipe_delete_btn);
        deleteRecipe.setOnClickListener((v)->{
            Model.instance.deleteRecipe(recipe,()->{
                Navigation.findNavController(recipeName).navigateUp();
            });
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
        String type = selectedCategory;
        String user = userName.getText().toString();
        String id = recipeNameAsId;

        Log.d("TAG", "name: " + recipe.getName());
        Recipe newRecipe = new Recipe(id,name,method,ingredients,type,user);

        Model.instance.editRecipe(newRecipe,()->{
            NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment(recipeNameAsId,usernameAsId));
        });

    //   NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2();//TODO:!!!!
    }

    private void initSpinnerFooter() {
        String[] items = new String[categories.size()];//TODO: why the size is 10?! instead of 5

        for(int i = 0 ; i<categories.size();i++){
            items[i] = categories.get(i);
        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        //categoriesSpinner.setAdapter(adapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                ((TextView) parent.getChildAt(0)).setTextSize(25);
                selectedCategory = items[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteRecipe){
            deleteRecipe();
            //TODO:delete
            // Model.instance.deleteRecipeNew(recipe.getId());

            //TODO:
           // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRecipe() {
//        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
//        builder.setPositiveButton("yes", ()->{
//
//        })
    }

}