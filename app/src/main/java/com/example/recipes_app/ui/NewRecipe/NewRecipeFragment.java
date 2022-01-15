package com.example.recipes_app.ui.NewRecipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class NewRecipeFragment extends Fragment{


    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_IMAGE = 2;

    EditText recipeName;
    EditText recipeMethod;
    EditText recipeIngredients;
    Button saveBtn;
    Spinner categoriesSpinner;
    List<String> categories= Model.instance.getAllCategories();
    String selectedCategory;
    Button cancelBtn;
    String usernameAsId;
    TextView username;

    Bitmap imageBitmap;
    ImageView recipeImage;
    ImageButton galleryBtn;
    ImageButton camBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        //usernameAsId = NewRecipeFragmentArgs.fromBundle(getArguments()).getUsername();
        //usernameAsId = Model.instance.GetCurrentNameUser();
        usernameAsId = Model.instance.getCurrentUsername();
        recipeName= view.findViewById(R.id.pe_nameOfRec);
        recipeMethod= view.findViewById(R.id.newRec_method);
        recipeIngredients= view.findViewById(R.id.newRec_ingredients);
        saveBtn = view.findViewById(R.id.newRec_save_btn);
        categoriesSpinner = view.findViewById(R.id.newRec_spinner);
        username = view.findViewById(R.id.newRec_username_tv);
        //categories = Model.instance.getAllCategories();
        initSpinnerFooter();
        recipeImage = view.findViewById(R.id.editmyaccount_image_recipe);
        username.setText(usernameAsId);
        cancelBtn = view.findViewById(R.id.newRec_cancel_btn);
        //progressBar = view.findViewById(R.id.main_progressbar);
        //progressBar.setVisibility(View.GONE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        camBtn = view.findViewById(R.id.newRec_camera_btn);

        galleryBtn = view.findViewById(R.id.newRec_gallery_btn);

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCam();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(NewRecipeFragmentDirections.actionGlobalMyAccountFragment(Model.instance.getCurrentUsername()));

            }
        });
        setHasOptionsMenu(true);


        return view;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA){
            if(resultCode== Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                recipeImage.setImageBitmap(imageBitmap);

            }
        }

       else if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        recipeImage.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void save() {
        //progressBar.setVisibility(View.VISIBLE);
        cancelBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String id = UUID.randomUUID().toString();
        String name = recipeName.getText().toString();
        String method = recipeMethod.getText().toString();
        String ingredients = recipeIngredients.getText().toString();

        String type = selectedCategory;

        Recipe recipe = new Recipe(id,name,method,ingredients,type,usernameAsId);
        //UserRecipe userRecipe = new UserRecipe(usernameAsId,name);

        if(imageBitmap == null){
            Model.instance.addRecipe(recipe,()->{
                Navigation.findNavController(recipeName).navigateUp();
            });

//            Model.instance.addUserRecipe(userRecipe,()->{
//
//            });
        }
        else{
            Model.instance.saveImage(imageBitmap,recipeName + ".jpg", url->{
                recipe.setImageUrl(url);
                Model.instance.addRecipe(recipe,()->{
                    Navigation.findNavController(recipeName).navigateUp();
                });

//                Model.instance.addUserRecipe(userRecipe,()->{
//
//                });
            });
        }



    }

    private void initSpinnerFooter() {
        String[] items = new String[categories.size()];

        for(int i = 0 ; i<categories.size();i++){
            items[i] = categories.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        categoriesSpinner.setAdapter(adapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(25);
                selectedCategory = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
