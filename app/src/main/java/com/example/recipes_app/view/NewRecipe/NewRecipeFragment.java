package com.example.recipes_app.view.NewRecipe;

import static android.graphics.Color.rgb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.MyApplication;
import com.example.recipes_app.R;
import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.view.MyAccount.UserViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    List<String> categories;
    String selectedCategory;
    Button cancelBtn;
    String usernameAsId;
    TextView username;

    Bitmap imageBitmap;
    ImageView recipeImage;
    ImageButton galleryBtn;
    ImageButton camBtn;
    ImageButton deleteImage;

    NewRecipeViewModel viewModel;
    UserViewModel userViewModel;

    ProgressBar progressBar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(NewRecipeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        categories= viewModel.getAllCategories();
        usernameAsId = viewModel.getCurrentUser();
        recipeName= view.findViewById(R.id.pe_nameOfRec);
        recipeMethod= view.findViewById(R.id.newRec_method);
        recipeIngredients= view.findViewById(R.id.newRec_ingredients);
        saveBtn = view.findViewById(R.id.newRec_save_btn);
        categoriesSpinner = view.findViewById(R.id.newRec_spinner);
        username = view.findViewById(R.id.newRec_username_tv);
        initSpinnerFooter();
        recipeImage = view.findViewById(R.id.newRecipe_image_recipe);
        username.setText(usernameAsId);

        progressBar = view.findViewById(R.id.newRec_progressbar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(255, 204, 204), android.graphics.PorterDuff.Mode.MULTIPLY);

        cancelBtn = view.findViewById(R.id.newRec_cancel_btn);

        recipeImage.setImageResource(R.drawable.img);
        saveBtn.setOnClickListener(v -> save());

        deleteImage = view.findViewById(R.id.deleteImg_btn);
        deleteImage.setOnClickListener(v -> deleteImage());

        camBtn = view.findViewById(R.id.newRec_camera_btn);

        galleryBtn = view.findViewById(R.id.newRec_gallery_btn);

        camBtn.setOnClickListener(v -> openCam());
        galleryBtn.setOnClickListener(v -> openGallery());
        cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        setHasOptionsMenu(true);

        return view;
    }

    private void deleteImage() {
        imageBitmap = null;
        recipeImage.setImageBitmap(null);
        recipeImage.setImageResource(R.drawable.img);
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

        if(imageBitmap == null){
            viewModel.addRecipe(recipe,()->{
                Navigation.findNavController(recipeName).navigateUp();
            });
        }
        else{
            viewModel.saveImage(imageBitmap,recipeName + ".jpg", url -> {
                recipe.setImageUrl(url);
                viewModel.addRecipe(recipe,()->{
                    Navigation.findNavController(recipeName).navigateUp();
                    saveImageFile(imageBitmap, recipeName + ".jpg", url1 -> { });
                });
            });

        }
    }


    public void saveImageFile(final Bitmap imageBitmap, String name, final ModelRecipe.SaveImageListener listener) {
        viewModel.saveImage(imageBitmap, name, url -> {
            String localName = getLocalImageFileName(url);
            Log.d("TAG","cach image: " + localName);
            saveImageToFile(imageBitmap,localName);
            listener.onComplete(url);
        });
    }

    public void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            addPictureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addPictureToGallery(File imageFile){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getContext().sendBroadcast(mediaScanIntent);
    }

    private String getLocalImageFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        return name;
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.myaccount_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            NavHostFragment.findNavController(this).navigate(NewRecipeFragmentDirections.actionGlobalMyAccountFragment(viewModel.getCurrentUser()));
            return true;
        }
         else if(item.getItemId() == R.id.logout_menu){
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
}
