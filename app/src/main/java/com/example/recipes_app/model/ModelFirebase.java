package com.example.recipes_app.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }



    public interface GetAllRecipesListener{
        void onComplete(List<Recipe> list);
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
                .document(recipe.getName())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getRecipeByRecipeName(String recipeName, Model.GetRecipeByRecipeName listener) {

        db.collection(Recipe.COLLECTION_NAME)
                .document(recipeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Recipe recipe = null;
                        if (task.isSuccessful() & task.getResult()!= null) {
                            recipe = Recipe.create(task.getResult().getData());
                        }
                        listener.onComplete(recipe);
                    }
                });
    }

    public void deleteRecipe(String recipeName, Model.DeleteRecipeListener listener) {
        db.collection(Recipe.COLLECTION_NAME)
                .document(recipeName)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
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

    public void addUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getUsername())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getUserByUsername(String username, Model.GetUserByUsername listener) {

        db.collection(User.COLLECTION_NAME)
                .document(username)
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
