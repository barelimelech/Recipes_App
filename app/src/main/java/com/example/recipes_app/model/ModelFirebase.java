package com.example.recipes_app.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    public void addRecipe(Recipe recipe, ModelRecipe.AddRecipeListener listener) {
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void editRecipe(Recipe recipe, ModelRecipe.EditRecipeListener listener) {
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getRecipeByRecipeName(String recipeId, ModelRecipe.GetRecipeByRecipeName listener) {
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

    public void deleteRecipe(Recipe recipe, ModelRecipe.DeleteRecipeListener listener) {
        recipe.setIsDeleted("true");
        Map<String, Object> json = recipe.toJson();
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipe.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void logout(String currentUserEmail, ModelUser.LogoutUserListener listener){
        ModelUser.instance.getUserByEmail(currentUserEmail, new ModelUser.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                User newUser = user;
                newUser.setIsConnected("false");
                ModelUser.instance.editUser(newUser, new ModelUser.EditUserListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });

            }

            @Override
            public void onFailure() { }
        });
    }

    /**
     * Firebase Storage
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public void saveImage(Bitmap imageBitmap, String imageName, ModelRecipe.SaveImageListener listener) {
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

    public void addUser(User user,String email, String password, ModelUser.AddUserListener listener) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setUId(firebaseAuth.getCurrentUser().getUid());
                Log.d("TAG", "saved name:" + user.fullName + "user Id:" + user.uId);

                Map<String, Object> json = user.toJson();
                db.collection(User.COLLECTION_NAME)
                        .document(user.getUId())
                        .set(json)
                        .addOnSuccessListener(unused -> listener.onComplete())
                        .addOnFailureListener(e -> listener.onComplete());
            }
            else {
                // If sign in fails, display a message to the user.
                listener.onFailure();
                Log.w("TAG", "createUserWithEmail:failure", task.getException());
            }
        });
    }

    public void signIn(User user,String email, String password,ModelUser.SigninUserListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setIsConnected("true");
                        listener.onComplete();
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success");

                    } else {
                        listener.onFailure();
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                    }
                });
    }


    public void getUserByEmail(String email, ModelUser.GetUserByEmail listener) {
        db.collection(User.COLLECTION_NAME)
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() & task.getResult()!= null && task.getResult().getDocuments().size()!=0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        db.collection(User.COLLECTION_NAME)
                                .document(documentId).get()
                                .addOnCompleteListener(task1 -> {
                                    User user= null;
                                    if (task1.isSuccessful() & task1.getResult() != null) {
                                        user = User.create(task1.getResult().getData());
                                    }
                                    listener.onComplete(user);
                                });
                    }
                    else{
                        listener.onFailure();
                    }
                });

    }

    public void editUser(User user, ModelUser.EditUserListener listener) {
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

}