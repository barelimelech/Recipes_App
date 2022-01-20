package com.example.recipes_app.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }


    public interface GetAllRecipesListener{
        void onComplete(List<Recipe> list);
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }
    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }


    public boolean isSignedIn(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null);
    }
    //TODO: fix since
    public void getAllRecipes(Long lastUpdateDate, GetAllRecipesListener listener) {
        db.collection(Recipe.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Recipe> list = new LinkedList<Recipe>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Recipe recipe = Recipe.create(doc.getData());
                            if (recipe != null){
                                list.add(recipe);
                            }
                        }
                    }
                    listener.onComplete(list);
                });

    }

    public void addRecipe(Recipe recipe, Model.AddRecipeListener listener) {
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void editRecipe(Recipe recipe, Model.EditRecipeListener listener) {
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getRecipeByRecipeName(String recipeId, Model.GetRecipeByRecipeName listener) {
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Recipe recipe = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            recipe = Recipe.create(task.getResult().getData());
                        }
                        listener.onComplete(recipe);
                    }
                });
    }

    public void deleteRecipe(Recipe recipe, Model.DeleteRecipeListener listener) {
        recipe.setIsDeleted("true");
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void logout(String currentUserEmail, Model.LogoutUserListener listener){
        Model.instance.getUserByEmail(currentUserEmail, new Model.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                User newUser = user;
                newUser.setIsConnected("false");
                Model.instance.editUser(newUser, new Model.EditUserListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });

            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * Firebase Storage
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("recipe_image/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
                });
    }

    //*******************************User*******************************//


    public interface GetAllUsersListener{
        void onComplete(List<User> list);
    }

    //TODO: fix since
    public void getAllUsers(Long lastUpdateDate, GetAllUsersListener listener) {
        db.collection(User.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<User>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            User user = User.create(doc.getData());
                            if (user != null){
                                list.add(user);
                            }
                        }
                    }
                    listener.onComplete(list);
                });

    }

//    public void addUser(User user, Model.AddUserListener listener) {
//        Map<String, Object> json = user.toJson();
//        db.collection(User.COLLECTION_NAME)
//                .document(user.getUId())
//                .set(json)
//                .addOnSuccessListener(unused -> listener.onComplete())
//                .addOnFailureListener(e -> listener.onComplete());
//    }
    public void addUser(User user,String email, String password, Model.AddUserListener listener) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.setUId(firebaseAuth.getCurrentUser().getUid());
                    Log.d("TAG", "saved name:" + user.fullName + "user Id:" + user.uId);

                    Map<String, Object> json = user.toJson();
                    //user.setIsConnected("true");
                    db.collection(User.COLLECTION_NAME)
                            .document(user.getUId())
                            .set(json)
                            .addOnSuccessListener(unused -> listener.onComplete())
                            .addOnFailureListener(e -> listener.onComplete());
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                    //Toast.makeText(, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signIn(User user,String email, String password,Model.SigninUserListener listener) {
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setIsConnected("true");

                            //AppLocalDb.db.userDao().insertAll(user);
                            listener.onComplete();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                           // FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                          //  startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } else {
                            listener.onFailure();
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(this, "User is not exist, please signup first.", Toast.LENGTH_LONG);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void getUserById(String uId, Model.GetUserById listener) {

        db.collection(User.COLLECTION_NAME)
                .document(uId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = null;
                        if (task.isSuccessful() & task.getResult()!= null) {
                            user = User.create(task.getResult().getData());
                        }
                        listener.onComplete(user);
                    }
                });
    }
    public void getUserByEmail(String email, Model.GetUserByEmail listener) {
        //Map<String, Object> json = recipe.toJson();
        db.collection(User.COLLECTION_NAME)
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() & task.getResult()!= null && task.getResult().getDocuments().size()!=0) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection(User.COLLECTION_NAME)
                                    .document(documentId).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            User user= null;
                                            if (task.isSuccessful() & task.getResult() != null) {
                                                user = User.create(task.getResult().getData());
                                            }
                                            listener.onComplete(user);
                                        }
                                    });
                        }
                        else{
                            listener.onFailure();
                        }
                    }
                });

    }

    public void editUser(User user, Model.EditUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getUId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }


        public String getUserId(){
        return firebaseAuth.getCurrentUser().getUid();
    }

//    public void getUserIdByEmail(String email, Model.GetUserByEmail listener){
//
//
//        db.collection(User.COLLECTION_NAME)
//                .document(uId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        User user = null;
//                        if (task.isSuccessful() & task.getResult()!= null) {
//                            user = User.create(task.getResult().getData());
//                        }
//                        listener.onComplete(user);
//                    }
//                });
//    }
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//    public String GetCurrentNameUser(){
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        return currentUser.getDisplayName();
//    }



    //*******************************UserRecipe*******************************//
    public interface GetAllUsersRecipesListener{
        void onComplete(List<UserRecipe> list);
    }

    //TODO: fix since
    public void getAllUsersRecipes(Long lastUpdateDate, GetAllUsersRecipesListener listener) {
        db.collection(UserRecipe.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<UserRecipe> list = new LinkedList<UserRecipe>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            UserRecipe userRecipe = UserRecipe.create(doc.getData());
                            if (userRecipe != null){
                                list.add(userRecipe);
                            }
                        }
                    }
                    listener.onComplete(list);
                });

    }

    public void addUserRecipe(UserRecipe userRecipe, Model.AddUserRecipeListener listener) {
        Map<String, Object> json = userRecipe.toJson();
        db.collection(UserRecipe.COLLECTION_NAME)
                .document(userRecipe.getRecipeNameAsId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }
    public void getUserRecipeByUsername(String username, Model.GetUserRecipeByUsername listener) {

        db.collection(UserRecipe.COLLECTION_NAME)
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserRecipe userRecipe = null;
                        if (task.isSuccessful() & task.getResult()!= null) {
                            userRecipe = UserRecipe.create(task.getResult().getData());
                        }
                        listener.onComplete(userRecipe);
                    }
                });
    }
}