package com.example.recipes_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

        EditText emailTv;
        EditText passwordTv;
    Button signUpBtn;
    Button loginBtn;
    EditText fullName;
    EditText phone;


    private GoogleSignInClient mGoogleSignInClient;

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailTv = this.findViewById(R.id.signup2_email_tv);
        passwordTv = findViewById(R.id.signup2_password_tv);

        signUpBtn =findViewById(R.id.signup2_signup_btn);
        loginBtn =findViewById(R.id.signup2_login_btn);
        fullName = findViewById(R.id.signup2_fullname_tv);
        phone = findViewById(R.id.signup2_phone_tv);
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTv.getText().toString();
                String password = passwordTv.getText().toString();
                String phone1 = phone.getText().toString();
                String fullName2 = fullName.getText().toString();
                createAccount(email,password);
                User user = new User(fullName2,phone1,email,"0");
                Model.instance.addUser(user, () -> {
                    //Navigation.findNavController(v).navigate(R.id.);

                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            }
        });



        //*****************************************************************//
        SignInButton authButton = findViewById(R.id.google_signin);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> { })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        Model.instance.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = getUId();
                            updateUI(userId);

                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

//    private void signIn(String email, String password) {
//        // [START sign_in_with_email]
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(SignupActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//        // [END sign_in_with_email]
//    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void reload() { }

    private void updateUI(String userId) {
        //Model.instance.getUserBId();
    }
    private String getUId() {
        return Model.instance.getUserId();
    }

    //**************************************************************//

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
//    if(mGoogleApiClient.isConnected()) {
//        showItem();
//    }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else{
                    Log.w("AUTH", "Account is NULL");
                    Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.w("AUTH", "Google sign in failed", e);
                Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Model.instance.connectWithGoogle(account, new Model.ConnectWithGoogle() {
//            @Override
//            public void onComplete() {
//                NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
//                navController.navigateUp();
//                navController.navigate(R.id.myAccount_nav);
//                Toast.makeText(MainActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
//            }
//        });
        Log.d("AUTH", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Model.instance.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "signInWithCredential:success");
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

//                            NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
//                            navController.navigateUp();
//                            navController.navigate(R.id.myAccount_nav);
                            Toast.makeText(SignupActivity.this, "Sign-in successful!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Sign-in failed, try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}

//package com.example.findmehomeapp.ui.Register;
//
//        import static android.app.Activity.RESULT_OK;
//
//        import android.app.Activity;
//        import android.app.AlertDialog;
//        import android.content.ContentValues;
//        import android.content.DialogInterface;
//        import android.content.Intent;
//        import android.graphics.Bitmap;
//        import android.net.Uri;
//        import android.os.Bundle;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//        import androidx.fragment.app.Fragment;
//        import androidx.navigation.NavController;
//        import androidx.navigation.Navigation;
//
//        import android.provider.MediaStore;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.AdapterView;
//        import android.widget.ArrayAdapter;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.ImageView;
//        import android.widget.Spinner;
//        import android.widget.Toast;
//
//        import com.example.findmehomeapp.Model.Model;
//        import com.example.findmehomeapp.Model.User;
//        import com.example.findmehomeapp.R;
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.OnSuccessListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.AuthResult;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.auth.FirebaseUser;
//        import com.google.firebase.firestore.FirebaseFirestore;
//        import com.google.firebase.storage.FirebaseStorage;
//        import com.google.firebase.storage.StorageReference;
//        import com.google.firebase.storage.UploadTask;
//
//        import java.io.IOException;
//        import java.util.HashMap;
//
//public class RegisterFragment extends Fragment {
//    // TODO: add image
//
//    EditText nameEt;
//    EditText phoneEt;
//    EditText emailEt;
//    EditText passwordEt;
//    EditText repasswordEt;
//    Spinner locationSpinner;
//    Button registerBtn;
//    NavController navController;
//    String userid;
//    String locationS;
//    ImageView picture;
//    ImageView addPicture;
//    Bitmap imageBitmap;
//
//
//    private static final int REQUEST_CAMERA = 1;
//    private static final int REQUEST_GALLERY = 2;
//
//
//    public RegisterFragment() {
//    }
//
//    //Executor executor = Executors.newFixedThreadPool(1);
//
//    //ModelFirebase modelFirebase = new ModelFirebase();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_register, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        picture = view.findViewById(R.id.register_user_imageView);
//        addPicture = view.findViewById(R.id.register_add_pic_imv);
//        nameEt = view.findViewById(R.id.register_et_name);
//        phoneEt = view.findViewById(R.id.register_phone_number);
//        emailEt = view.findViewById(R.id.register_et_email);
//        passwordEt = view.findViewById(R.id.register_et_password);
//        repasswordEt = view.findViewById(R.id.register_et_repassword);
//
//        locationSpinner = view.findViewById(R.id.register_location_spinner);
//        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this.getContext(),
//                R.array.location, android.R.layout.simple_spinner_item);
//        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        locationSpinner.setAdapter(adapterGender);
//        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                locationS = parent.getItemAtPosition(position).toString();
//                //Toast.makeText(parent.getContext(), gender, Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        addPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImagePickDialog();
//            }
//        });
//
//        registerBtn = view.findViewById(R.id.register_btn_register);
//        navController = Navigation.findNavController(view);
//
//        registerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // validateAndSave();
//
//                String email = emailEt.getText().toString().trim();
//                String password = passwordEt.getText().toString().trim();
//                String fullName = nameEt.getText().toString();
//                String phone = phoneEt.getText().toString();
//                String repassword = repasswordEt.getText().toString();
//
//
//                //TODO:tell the user if his email already exist he can't register
//                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(getContext(), "Fields are required", Toast.LENGTH_SHORT).show();
//                }
//
//                if (fullName.isEmpty()) {
//                    nameEt.setError("Enter a name");
//                }
//
//                if (password.length() < 6) {
//                    passwordEt.setError("Password Length Must Be 6 or more Chars");
//                }
//
////                if(!password.equals(repassword)){
////                    passwordEt.setError("Password and Re-password need to be the same");
////                }
//
//                if (email.isEmpty()) {
//                    emailEt.setError("Enter email Bitch");
//                }
//
//                if (phone.isEmpty()) {
//                    phoneEt.setError("Enter a phone");
//                }
//
//                registerUser(fullName, email, password, phone, locationS);
//
//            }
//        });
//
//
////        gotoSignIn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                navController.navigate(R.id.action_registerFragment_to_loginFragment);
////            }
////        });
//
//
//    }
//
//    private void showImagePickDialog() {
//
//        String[] items = {"Camera", "Gallery"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//        builder.setTitle("Choose an Option");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                if (i == 0) {
//                    openCam();
//                }
//
//                if (i == 1) {
//                    openGallery();
//                }
//            }
//        });
//
//        builder.create().show();
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
//    }
//
//    private void openCam() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CAMERA) {
//            if (resultCode == Activity.RESULT_OK) {
//                Bundle extras = data.getExtras();
//                imageBitmap = (Bitmap) extras.get("data");
//                picture.setImageBitmap(imageBitmap);
//                Log.d("TAG33", "imageBitmap name:" + imageBitmap);
//
//            }
//        }
//        if (requestCode == REQUEST_GALLERY) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    try {
//                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
//                        picture.setImageBitmap(imageBitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//
//    private void registerUser(String name, String email, String password, String phone, String location) {
//
//        User user = new User("0", name, phone, email, password, location, "0");
//
//        if (imageBitmap == null) {
//            Model.instance.addUser(user, () -> {
//                navController.navigate(R.id.action_global_nav_profile);
//
//            });
//        } else {
//            Model.instance.saveImage(imageBitmap, email + ".jpg", url -> {
//                user.setAvatarUrl(url);
//                Model.instance.addUser(user, () -> {
//                    navController.navigate(R.id.action_global_nav_profile);
//                });
//            });
//        }
//    }
//}