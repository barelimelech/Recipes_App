package com.example.recipes_app.view.EditRecipe;

import static android.graphics.Color.rgb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.view.MyAccount.UserViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class EditRecipeFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_IMAGE = 2;

    TextView recipeName;
    TextView recipeMethod;
    TextView recipeIngredients;
    TextView userName;
    Recipe recipe;
    Button saveRecipe;
    Button backBtn;
    String recipeNameAsId, usernameAsId;
    Spinner categoriesSpinner;
    List<String> categories = Model.instance.getAllCategories();
    String selectedCategory;
    ImageButton deleteImage;
    Bitmap imageBitmap;
    ImageView recipeImage;
    ImageButton galleryBtn;
    ImageButton camBtn;

    EditRecipeViewModel viewModel;
    UserViewModel userViewModel;

    ProgressBar progressBar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EditRecipeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        recipeNameAsId = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipeId();
        usernameAsId = EditRecipeFragmentArgs.fromBundle(getArguments()).getUsername();
        selectedCategory = EditRecipeFragmentArgs.fromBundle(getArguments()).getCategory();
        recipeName = view.findViewById(R.id.editrecipe_name_tv);
        recipeMethod= view.findViewById(R.id.editrecipe_method_tv);
        recipeIngredients= view.findViewById(R.id.editrecipe_ingredients_yv);
        categoriesSpinner= view.findViewById(R.id.editrecipe_spinner);
        userName = view.findViewById(R.id.editrecipe_username_tv2);
        userName.setText("By: "+usernameAsId);
        recipeImage = view.findViewById(R.id.editrecipe_image_recipe);
        progressBar = view.findViewById(R.id.edit_progressbar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(255, 204, 204), android.graphics.PorterDuff.Mode.MULTIPLY);

        recipe = new Recipe();

        viewModel.getRecipeByRecipeName(recipeNameAsId, recipe -> {
            recipeName.setText(recipe.getName());
            recipeMethod.setText(recipe.getMethod());
            recipeIngredients.setText(recipe.getIngredients());
            if(recipe.getRecipeUrl()!=null){
                Picasso.get().load(recipe.getRecipeUrl()).into(recipeImage);
            }
            else{
                recipeImage.setImageResource(R.drawable.cake);
            }
            imageBitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
        });


        recipeImage.setImageBitmap(imageBitmap);
        initSpinnerFooter();

        saveRecipe = view.findViewById(R.id.editrecipe_save_btn);
        saveRecipe.setOnClickListener(v -> save());

        deleteImage = view.findViewById(R.id.deleteImg_btn);
        deleteImage.setOnClickListener(v -> deleteImage());

        camBtn = view.findViewById(R.id.editRec_camera_btn);
        camBtn.setOnClickListener(v -> openCam());

        galleryBtn = view.findViewById(R.id.editRec_gallery_btn);
        galleryBtn.setOnClickListener(v -> openGallery());



        backBtn = view.findViewById(R.id.editrecipe_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });



        setHasOptionsMenu(true);
        return view;
    }
    private void deleteImage() {
        imageBitmap = null;
        recipeImage.setImageBitmap(null);
        recipeImage.setImageResource(R.drawable.cake);
        viewModel.refreshRecipesList();
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
        progressBar.setVisibility(View.VISIBLE);
        saveRecipe.setEnabled(false);
        backBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String name = recipeName.getText().toString();
        Log.d("TAG", "name: " + name);
        String method = recipeMethod.getText().toString();
        String ingredients = recipeIngredients.getText().toString();
        String type = selectedCategory;
        String user = userName.getText().toString();
        String id = recipeNameAsId;

        Log.d("TAG", "name: " + recipe.getName());
        Recipe newRecipe = new Recipe(id,name,method,ingredients,type,user);

        if(imageBitmap == null){
            viewModel.editRecipe(newRecipe,()->{
                Navigation.findNavController(getView()).navigateUp();
            });

        }
        else{

            viewModel.saveImage(imageBitmap,recipeName + ".jpg",url->{
                newRecipe.setImageUrl(url);
                viewModel.editRecipe(newRecipe, ()->{
                    Navigation.findNavController(getView()).navigateUp();

                });
            } );

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = userViewModel.getCurrentUserEmail();
            userViewModel.signOut();
            userViewModel.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });

            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onResume() {
      super.onResume();
        String  myCategory;
        int categoryPos = -1;

        myCategory = selectedCategory;
        if(myCategory != null) {
            switch (myCategory) {
                case "Desserts":
                    categoryPos = 0;
                    break;

                case "Breakfast":
                    categoryPos = 1;
                    break;

                case "Lunch":
                    categoryPos = 2;
                    break;

                case "Dinner":
                    categoryPos = 3;
                    break;

                case "Holidays":
                    categoryPos = 4;
                    break;

            }
        }
        categoriesSpinner.setSelection(categoryPos);
    }

}