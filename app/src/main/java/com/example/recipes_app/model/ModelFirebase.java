package com.example.recipes_app.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getAllRecipes(Model.GetAllRecipesListener listener) {
        db.collection(Recipe.COLLECTION_NAME)
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

    public void getRecipeById(String recipeId, Model.GetRecipeById listener) {

        db.collection(Recipe.COLLECTION_NAME)
                .document(recipeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Recipe recipe = null;
                        if (task.isSuccessful() & task.getResult()!= null){
                            recipe = Recipe.create(task.getResult().getData());
                        }
                        listener.onComplete(recipe);
                    }
                });
    }
}
