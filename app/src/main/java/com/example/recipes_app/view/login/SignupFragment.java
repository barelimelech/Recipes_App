package com.example.recipes_app.view.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class SignupFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_IMAGE = 2;

    EditText emailTv;
    EditText passwordTv;
    Button signUpBtn;
    Button loginBtn;
    EditText fullName;
    EditText phone;

    Bitmap imageBitmap;
    ImageView userImage;
    ImageButton galleryBtn;
    ImageButton camBtn;

    private GoogleSignInClient mGoogleSignInClient;

    GoogleApiClient mGoogleApiClient;


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
       // loginBtn =view.findViewById(R.id.signup22_login_btn);
        fullName = view.findViewById(R.id.signup22_fullname_tv);
        phone = view.findViewById(R.id.signup22_phone_tv);
        userImage = view.findViewById(R.id.singup_image_user);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//
//            }
//        });

        camBtn = view.findViewById(R.id.signup_camera_btn);

        galleryBtn = view.findViewById(R.id.signup_gallery_btn);

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

    private void save() {
        //signUpBtn.setEnabled(false);
        String email = emailTv.getText().toString();
        String password = passwordTv.getText().toString();
        String phone1 = phone.getText().toString();
        String fullName2 = fullName.getText().toString();

        if(TextUtils.isEmpty(fullName2)) {
            fullName.setError("Please Enter full name :)");
        }else if(TextUtils.isEmpty(phone1)) {
            phone.setError("Please Enter phone number :)");
        }else if (phone1.length() < 10) {
            phone.setError("Phone must be 10 or more characters");
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
                Model.instance.addUser(user, email, password, () -> {
                    //Navigation.findNavController(v).navigate(R.id.);
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                });
            }
            else{
                Model.instance.saveImage(imageBitmap,fullName + ".jpg", url-> {
                    user.setUserUrl(url);
                    Model.instance.addUser(user, email, password, () -> {
                        //Navigation.findNavController(v).navigate(R.id.);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    });
                });
            }
        }
    }
}