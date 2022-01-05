package com.example.recipes_app.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
