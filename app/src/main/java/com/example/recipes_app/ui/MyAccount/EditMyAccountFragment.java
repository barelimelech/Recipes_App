package com.example.recipes_app.ui.MyAccount;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.example.recipes_app.ui.RecipesList.RecipesListFragmentDirections;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class EditMyAccountFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_IMAGE = 2;

    TextView email;
    TextView password;
    TextView fullName;
    TextView phone;

    Button saveMyAccount;
    Button cancelBtn;


    ImageButton deleteImage;
    Bitmap imageBitmap;
    ImageView userImage;
    ImageButton galleryBtn;
    ImageButton camBtn;

    String usernameAsId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_my_account, container, false);

        email = view.findViewById(R.id.editmyaccount_email_tv);

        phone = view.findViewById(R.id.editmyaccount_phone_tv);
        password= view.findViewById(R.id.editmyaccount_newpassword_tv);
        fullName= view.findViewById(R.id.editmyaccount_fullname_tv);
        cancelBtn = view.findViewById(R.id.editmyaccount_cancel_btn);
        userImage = view.findViewById(R.id.editmyaccount_image_recipe);
        //usernameAsId = EditMyAccountFragmentArgs.fromBundle(getArguments()).getUsername();

        Model.instance.getUserByEmail(Model.instance.getCurrentUserEmail(), new Model.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                if(user.getUserUrl()!=null){
                    Picasso.get().load(user.getUserUrl()).into(userImage);
                }
                imageBitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
            }
        });

        userImage.setImageBitmap(imageBitmap);
//        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
//
//            @Override
//            public void onComplete(User user) {
//                fullName.setText(user.getFullName());
//                password.setText(user.getPassword());
//                username.setText(user.getUsername());
//
//            }
//        });


        saveMyAccount = view.findViewById(R.id.editmyaccount_save_btn);
        saveMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();

            }
        });

        deleteImage = view.findViewById(R.id.deleteImg_btn3);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });

        camBtn = view.findViewById(R.id.editUser_camera_btn);

        galleryBtn = view.findViewById(R.id.editUser_gallery_btn);

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

//
//        backBtn = view.findViewById(R.id.editrecipe_back_btn);
//        backBtn.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigateUp();
//        });
        setHasOptionsMenu(true);
        return view;
    }

    private void deleteImage() {
        imageBitmap = null;
        userImage.setImageBitmap(null);
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
                userImage.setImageBitmap(imageBitmap);

            }
        }

        else if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        userImage.setImageBitmap(imageBitmap);
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
        saveMyAccount.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        //backBtn.setEnabled(false);
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        String fullName1 = fullName.getText().toString();
        String phone1 = phone.getText().toString();
        if(!password1.equals("")) {
            Model.instance.getCurrentUser().updatePassword(password1);
        }
        User newUser = new User(fullName1,phone1,email1,Model.instance.getUserId());

        if(imageBitmap == null){
            Model.instance.editUser(newUser,new Model.EditUserListener(){
                @Override
                public void onComplete() {
                    Navigation.findNavController(getView()).navigateUp();
                }
            });
        }
        else {
            Model.instance.saveImage(imageBitmap,fullName + ".jpg", url-> {
                newUser.setUserUrl(url);
                Model.instance.editUser(newUser, new Model.EditUserListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(getView()).navigateUp();
                    }
                });
            });
        }


       // NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));

//        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
//            @Override
//            public void onComplete(Recipe student) {
//                student.setName(name);
//                student.setMethod(method);
//                student.setIngredients(ingredients);
//            }
//        });
//        Recipe updatedRecipe = new Recipe();
//        updatedRecipe.setName(name);
//        Log.d("TAG", "name: " + updatedRecipe.getName());
//        updatedRecipe.setMethod(method);
//        updatedRecipe.setIngredients(ingredients);
//        Map<String, Object> json = updatedRecipe.toJson();
//        db.collection("Recipe.COLLECTION_NAME")
//                .document(recipeId)
//                .set(json);
       // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionGlobalMyAccountFragment(Model.instance.getCurrentUsername()));
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
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}