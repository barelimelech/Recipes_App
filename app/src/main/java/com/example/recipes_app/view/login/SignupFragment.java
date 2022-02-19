package com.example.recipes_app.view.login;

import static android.graphics.Color.rgb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipes_app.MainActivity;
import com.example.recipes_app.MyApplication;
import com.example.recipes_app.R;
import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.User;
import com.example.recipes_app.view.MyAccount.UserViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SignupFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_IMAGE = 2;

    EditText emailTv;
    EditText passwordTv;
    Button signUpBtn;
    EditText fullName;
    EditText phone;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailTv = view.findViewById(R.id.signup22_email_tv);
        passwordTv = view.findViewById(R.id.signup22_password_tv);

        signUpBtn =view.findViewById(R.id.signup22_signup_btn);
        fullName = view.findViewById(R.id.signup22_fullname_tv);
        phone = view.findViewById(R.id.signup22_phone_tv);
        userImage = view.findViewById(R.id.singup_image_user);

        progressBar = view.findViewById(R.id.singup_progressbar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(255, 204, 204), android.graphics.PorterDuff.Mode.MULTIPLY);

        signUpBtn.setOnClickListener(v -> save());

        camBtn = view.findViewById(R.id.signup_camera_btn);
        camBtn.setOnClickListener(v -> openCam());

        galleryBtn = view.findViewById(R.id.signup_gallery_btn);
        galleryBtn.setOnClickListener(v -> openGallery());

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
    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void save() {
        String email = emailTv.getText().toString().toLowerCase();
        progressBar.setVisibility(View.GONE);
        String password = passwordTv.getText().toString();
        String phone1 = phone.getText().toString();
        String fullName2 = fullName.getText().toString();

        if(TextUtils.isEmpty(fullName2)) {
            fullName.setError("Please Enter full name :)");
        }else if(TextUtils.isEmpty(phone1)) {
            phone.setError("Please Enter phone number :)");
        } else if (TextUtils.isEmpty(email)){
            emailTv.setError("Please Enter email :)");
        }else if(!email.contains("@")){
            emailTv.setError("Email must contain '@' :)");
        }else if(TextUtils.isEmpty(password)){
            passwordTv.setError("Please Enter password :)");
        }else if (password.length() < 6) {
            passwordTv.setError("Password must be 6 or more characters");
        } else {
            User user = new User(fullName2, phone1, email, "0");
            if (imageBitmap == null) {

                viewModel.addUser(user, email, password, new ModelUser.AddUserListener() {
                    @Override
                    public void onComplete() { toFeedActivity(); }
                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "Email is already exist.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else{
                viewModel.saveImage(imageBitmap,fullName + ".jpg", url-> {
                    user.setUserUrl(url);
                    saveImageFile(imageBitmap, fullName + ".jpg", url1 -> {});
                    viewModel.addUser(user, email, password, new ModelUser.AddUserListener() {
                        @Override
                        public void onComplete() { toFeedActivity(); }
                        @Override
                        public void onFailure() {
                            Toast.makeText(getActivity(), "Email is already exist.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                });

            }
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
}