//package com.example.recipes_app.ui.MyAccount;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.recipes_app.R;
//import com.example.recipes_app.model.Model;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//public class EditMyAccountFragment extends Fragment {
//    TextView username;
//    TextView password;
//    TextView fullName;
//    Button saveMyAccount;
//    private FirebaseFirestore db;
//
//    String usernameAsId;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_edit_my_account, container, false);
//
//
//        username = view.findViewById(R.id.editmyaccount_username_tv);
//        password= view.findViewById(R.id.editmyaccount_password_tv);
//        fullName= view.findViewById(R.id.editmyaccount_fullname_tv);
//        db = FirebaseFirestore.getInstance();
//        usernameAsId = EditMyAccountFragmentArgs.fromBundle(getArguments()).getUsername();
//
//
////        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
////
////            @Override
////            public void onComplete(User user) {
////                fullName.setText(user.getFullName());
////                password.setText(user.getPassword());
////                username.setText(user.getUsername());
////
////            }
////        });
//
//
//        saveMyAccount = view.findViewById(R.id.editmyaccount_save_btn);
//        saveMyAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                save();
//            }
//        });
//
//
////
////        backBtn = view.findViewById(R.id.editrecipe_back_btn);
////        backBtn.setOnClickListener((v)->{
////            Navigation.findNavController(v).navigateUp();
////        });
//        setHasOptionsMenu(true);
//        return view;
//    }
//
//    private void save() {
//        saveMyAccount.setEnabled(false);
//        //backBtn.setEnabled(false);
//        String username1 = username.getText().toString();
//        String password1 = password.getText().toString();
//        String fullName1 = fullName.getText().toString();
//        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
//
//            @Override
//            public void onComplete(User user) {
//                user.setFullName(fullName1);
//                user.setUsername(username1);
//                user.setPassword(password1);
//            }
//        });
//
//       // NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));
//
////        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
////            @Override
////            public void onComplete(Recipe student) {
////                student.setName(name);
////                student.setMethod(method);
////                student.setIngredients(ingredients);
////            }
////        });
////        Recipe updatedRecipe = new Recipe();
////        updatedRecipe.setName(name);
////        Log.d("TAG", "name: " + updatedRecipe.getName());
////        updatedRecipe.setMethod(method);
////        updatedRecipe.setIngredients(ingredients);
////        Map<String, Object> json = updatedRecipe.toJson();
////        db.collection("Recipe.COLLECTION_NAME")
////                .document(recipeId)
////                .set(json);
//       // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2());
//
//
//    }
//}