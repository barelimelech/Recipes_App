package com.example.recipes_app.ui.newRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.model.UserRecipe;

import java.util.List;

public class NewRecipeFragment extends Fragment{



    EditText recipeName;
    EditText recipeMethod;
    EditText recipeIngredients;
    Button saveBtn;
    Spinner categoriesSpinner;
    List<String> categories= Model.instance.getAllCategories();
    String selectedCategory;
    //Button cancelBtn;

    String usernameAsId;
    TextView username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        usernameAsId = NewRecipeFragmentArgs.fromBundle(getArguments()).getUsername();

        recipeName= view.findViewById(R.id.pe_nameOfRec);
        recipeMethod= view.findViewById(R.id.newRec_method);
        recipeIngredients= view.findViewById(R.id.newRec_ingredients);
        saveBtn = view.findViewById(R.id.newRec_save_btn);
        categoriesSpinner = view.findViewById(R.id.newRec_spinner);
        username = view.findViewById(R.id.newRec_username_tv);
        //categories = Model.instance.getAllCategories();
        initSpinnerFooter();

        username.setText(usernameAsId);
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
        //cancelBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        String name = recipeName.getText().toString();
        String method = recipeMethod.getText().toString();
        String ingredients = recipeIngredients.getText().toString();
        String type = selectedCategory;


        Recipe recipe = new Recipe(name,method,ingredients,type);
        UserRecipe userRecipe = new UserRecipe(usernameAsId,name);

        Model.instance.addRecipe(recipe,()->{
            Navigation.findNavController(recipeName).navigateUp();
        });

        Model.instance.addUserRecipe(userRecipe,()->{

        });

    }

    private void initSpinnerFooter() {
        String[] items = new String[categories.size()];//TODO: why the size is 10?! instead of 5

        for(int i = 0 ; i<categories.size();i++){
            items[i] = categories.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        categoriesSpinner.setAdapter(adapter);
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
}
