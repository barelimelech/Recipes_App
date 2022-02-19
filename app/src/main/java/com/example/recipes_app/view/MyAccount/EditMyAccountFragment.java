package com.example.recipes_app.view.MyAccount;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.User;
import com.example.recipes_app.view.RecipesList.RecipesListFragmentDirections;
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

    ProgressBar progressBar;

    UserViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


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

        progressBar = view.findViewById(R.id.editmyaccount_progressbar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(255, 204, 204), android.graphics.PorterDuff.Mode.MULTIPLY);

        viewModel.getUserByEmail(viewModel.getCurrentUserEmail(), new ModelUser.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                if(user.getUserUrl()!=null){
                    Picasso.get().load(user.getUserUrl()).into(userImage);
                    imageBitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
                }
                else{
                    userImage.setImageResource(R.drawable.avatar);
                }
            }

            @Override
            public void onFailure() { }
        });


        userImage.setImageBitmap(imageBitmap);

        saveMyAccount = view.findViewById(R.id.editmyaccount_save_btn);
        saveMyAccount.setOnClickListener(v -> save());

        cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        deleteImage = view.findViewById(R.id.deleteImg_btn3);
        deleteImage.setOnClickListener(v -> deleteImage());

        camBtn = view.findViewById(R.id.editUser_camera_btn);
        camBtn.setOnClickListener(v -> openCam());

        galleryBtn = view.findViewById(R.id.editUser_gallery_btn);
        galleryBtn.setOnClickListener(v -> openGallery());

        setHasOptionsMenu(true);
        return view;
    }

    private void deleteImage() {
        imageBitmap = null;
        userImage.setImageBitmap(null);
        userImage.setImageResource(R.drawable.avatar);
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
        progressBar.setVisibility(View.VISIBLE);
        saveMyAccount.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        String fullName1 = fullName.getText().toString();
        String phone1 = phone.getText().toString();
        if(!password1.equals("")) {
            viewModel.updatePassword(password1);
        }
        User newUser = new User(fullName1,phone1,email1,viewModel.getUserId());

        if(imageBitmap == null){
            viewModel.editUser(newUser, () -> Navigation.findNavController(getView()).navigateUp());
        }
        else {
            viewModel.saveImage(imageBitmap,fullName + ".jpg", url -> {
                newUser.setUserUrl(url);
                viewModel.editUser(newUser, () -> Navigation.findNavController(getView()).navigateUp());
            } );

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(ModelUser.instance.getCurrentUsername()));
            return true;
        }else if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = ModelUser.instance.getCurrentUserEmail();
            ModelUser.instance.getFirebaseAuth().signOut();
            ModelUser.instance.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });

            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.myaccount_menu, menu);

    }
}