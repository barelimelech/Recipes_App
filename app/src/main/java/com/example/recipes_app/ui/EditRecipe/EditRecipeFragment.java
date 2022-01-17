package com.example.recipes_app.ui.EditRecipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
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
    Button deleteRecipe;
    String recipeNameAsId, usernameAsId, category;
    Spinner categoriesSpinner;
    List<String> categories = Model.instance.getAllCategories();
    String selectedCategory;
    ImageButton deleteImage;
    Bitmap imageBitmap;
    ImageView recipeImage;
    ImageButton galleryBtn;
    ImageButton camBtn;

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
        recipeImage = view.findViewById(R.id.editrecipe_image_recipe);


        recipe = new Recipe();

        Model.instance.getRecipeByRecipeName(recipeNameAsId, new Model.GetRecipeByRecipeName() {
            @Override
            public void onComplete(Recipe recipe) {
                recipeName.setText(recipe.getName());
                recipeMethod.setText(recipe.getMethod());
                recipeIngredients.setText(recipe.getIngredients());
                userName.setText(recipe.getUsername());
                if(recipe.getRecipeUrl()!=null){
                    Picasso.get().load(recipe.getRecipeUrl()).into(recipeImage);
                }
                imageBitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
            }
        });

        recipeImage.setImageBitmap(imageBitmap);
        initSpinnerFooter();

        saveRecipe = view.findViewById(R.id.editrecipe_save_btn);
        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        deleteImage = view.findViewById(R.id.deleteImg_btn);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });

//        if(recipeImage.getDrawable() == null){
//            deleteImage.setVisibility(View.GONE);
//        }

        camBtn = view.findViewById(R.id.editRec_camera_btn);

        galleryBtn = view.findViewById(R.id.editRec_gallery_btn);

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



        backBtn = view.findViewById(R.id.editrecipe_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });


//
//        deleteRecipe = view.findViewById(R.id.editrecipe_delete_btn);
////        viewModel.deleteRecipe(recipe, ()->{
////            Model.instance.refreshRecipeList();
////        });
//        deleteRecipe.setOnClickListener((v)->{
//             delete();
//            Model.instance.deleteRecipe(recipe,()->{
//                Log.d("TAG", "name: " + recipe);
//                Model.instance.refreshRecipeList();
//                NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment(usernameAsId,recipeNameAsId));
//                //NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalMyAccountFragment(recipe.getUsername()));//TODO:!!!!
//            });
//        });

        setHasOptionsMenu(true);
        return view;
    }
    private void deleteImage() {
        imageBitmap = null;
        recipeImage.setImageBitmap(null);
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


//    private void delete() {
//        deleteRecipe.setEnabled(false);
//
//        String name = recipeName.getText().toString();
//        Log.d("TAG", "name: " + name);
//        String method = recipeMethod.getText().toString();
//        String ingredients = recipeIngredients.getText().toString();
//        String type = selectedCategory;
//        String user = userName.getText().toString();
//        String id = recipeNameAsId;
//
//        Log.d("TAG", "name: " + recipe.getName());
//        Recipe newRecipe = new Recipe(id,name,method,ingredients,type,user);
//        newRecipe.setIsDeleted("true");
//        recipe = newRecipe;
//
//    }

    //TODO:save..
    private void save() {
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
            Model.instance.editRecipe(newRecipe,()->{
                Navigation.findNavController(getView()).navigateUp();
            });
        }
        else{
            Model.instance.saveImage(imageBitmap,recipeName + ".jpg", url->{
                newRecipe.setImageUrl(url);
                Model.instance.editRecipe(newRecipe,()->{
                    Navigation.findNavController(getView()).navigateUp();
                });
            });
        }

//        Model.instance.editRecipe(newRecipe,()->{
//           // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment(usernameAsId,recipeNameAsId));
//            Navigation.findNavController(getView()).navigateUp();
//        });

    //   NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2();//TODO:!!!!
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
        }else if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = Model.instance.getCurrentUserEmail();
            Model.instance.getFirebaseAuth().signOut();
            Model.instance.logout(currentUserEmail, new Model.LogoutUserListener() {
                @Override
                public void onComplete() {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });

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

//    @Override
//    public void onResume() {
//        super.onResume();
//        Model.instance.getUserById(userId, new Model.GetUserById() {
//            @Override
//            public void onComplete(User user) {
//                myLocation = user.getLocation();
//                switch (myLocation) {
//                    case "Center":
//                        locationPos = 0;
//                        break;
//
//                    case "North":
//                        locationPos = 1;
//                        break;
//
//                    case "South":
//                        locationPos = 2;
//                        break;
//
//                }
//                locationSpinner.setSelection(locationPos);
//            }
//        });
//    }

}