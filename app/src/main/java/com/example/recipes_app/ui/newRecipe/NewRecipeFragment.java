package com.example.recipes_app.ui.newRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

public class NewRecipeFragment extends Fragment {



    EditText recipeName;
    EditText recipeMethod;
    EditText recipeIngredients;
    EditText recipeId;
    EditText recipeType;
    Button saveBtn;
    //Button cancelBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        recipeName= view.findViewById(R.id.pe_nameOfRec);
        recipeMethod= view.findViewById(R.id.newRec_method);
        recipeIngredients= view.findViewById(R.id.newRec_ingredients);
        recipeId= view.findViewById(R.id.newRec_id);
        recipeType= view.findViewById(R.id.newRec_recType_rv);
        saveBtn = view.findViewById(R.id.newRec_save_btn);

        //cancelBtn = view.findViewById(R.id.main_cancel_btn);
        //progressBar = view.findViewById(R.id.main_progressbar);
        //progressBar.setVisibility(View.GONE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        return view;
    }


    private void save() {
        //progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        //cancelBtn.setEnabled(false);
        String name = recipeName.getText().toString();
        String method = recipeMethod.getText().toString();
        String ingredients = recipeIngredients.getText().toString();
        String id = recipeId.getText().toString();
        String type = recipeType.getText().toString();


        Recipe recipe = new Recipe(name,method,ingredients,id,type);
        Model.instance.addRecipe(recipe,()->{
            Navigation.findNavController(recipeId).navigateUp();
        });

    }
}