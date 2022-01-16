package com.example.recipes_app.ui.MyAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.example.recipes_app.ui.RecipesList.RecipesListFragmentDirections;


public class EditMyAccountFragment extends Fragment {
    TextView email;
    TextView password;
    TextView fullName;
    TextView phone;

    Button saveMyAccount;
    Button cancelBtn;

    String usernameAsId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_my_account, container, false);

        email = view.findViewById(R.id.editmyaccount_email_tv);

        phone = view.findViewById(R.id.editmyaccount_phone_tv);
        password= view.findViewById(R.id.editmyaccount_newpassword_tv);
        fullName= view.findViewById(R.id.editmyaccount_fullname_tv);
        cancelBtn = view.findViewById(R.id.editmyaccount_cancel_btn);
        //usernameAsId = EditMyAccountFragmentArgs.fromBundle(getArguments()).getUsername();

        Model.instance.getUserByEmail(Model.instance.getCurrentUserEmail(), new Model.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
            }
        });


//        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
//
//            @Override
//            public void onComplete(User user) {
//                fullName.setText(user.getFullName());
//                password.setText(user.getPassword());
//                username.setText(user.getUsername());
//
//            }
//        });


        saveMyAccount = view.findViewById(R.id.editmyaccount_save_btn);
        saveMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();

            }
        });

//
//        backBtn = view.findViewById(R.id.editrecipe_back_btn);
//        backBtn.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigateUp();
//        });
        setHasOptionsMenu(true);
        return view;
    }

    private void save() {
        saveMyAccount.setEnabled(false);
        //backBtn.setEnabled(false);
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        String fullName1 = fullName.getText().toString();
        String phone1 = phone.getText().toString();
        Model.instance.getCurrentUser().updatePassword(password1);
        User newUser = new User(fullName1,phone1,email1,Model.instance.getUserId());
        Model.instance.editUser(newUser, new Model.EditUserListener() {
            @Override
            public void onComplete() {
                Navigation.findNavController(getView()).navigateUp();
            }
        });


       // NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));

//        Model.instance.getRecipeById(recipeId, new Model.GetRecipeById() {
//            @Override
//            public void onComplete(Recipe student) {
//                student.setName(name);
//                student.setMethod(method);
//                student.setIngredients(ingredients);
//            }
//        });
//        Recipe updatedRecipe = new Recipe();
//        updatedRecipe.setName(name);
//        Log.d("TAG", "name: " + updatedRecipe.getName());
//        updatedRecipe.setMethod(method);
//        updatedRecipe.setIngredients(ingredients);
//        Map<String, Object> json = updatedRecipe.toJson();
//        db.collection("Recipe.COLLECTION_NAME")
//                .document(recipeId)
//                .set(json);
       // NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment2());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionGlobalMyAccountFragment(Model.instance.getCurrentUsername()));
            return true;
        }else if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = Model.instance.getCurrentUserEmail();
            Model.instance.getFirebaseAuth().signOut();
            Model.instance.logout(currentUserEmail, new Model.LogoutUserListener() {
                @Override
                public void onComplete() {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });

            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}