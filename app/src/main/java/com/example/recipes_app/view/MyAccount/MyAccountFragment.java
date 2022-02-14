package com.example.recipes_app.view.MyAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.squareup.picasso.Picasso;

public class MyAccountFragment extends Fragment {


    TextView fullName;

    Button newRecipe;
    Button logOutBtn;
    View view;
    String fullNameAsId;
    String currentUserEmail;
    ImageView userImage;

    UserViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_account, container, false);
        userImage = view.findViewById(R.id.myAccount_image_user);

        viewModel.getUserByEmail(viewModel.getCurrentUserEmail(), new Model.GetUserByEmail() {
            @Override
            public void onComplete(User user) {
                fullName.setText(user.getFullName());
                if(user.getUserUrl()!=null) {
                    Picasso.get().load(user.getUserUrl()).into(userImage);
                }
            }
            @Override
            public void onFailure() {

            }
        });

        fullName = view.findViewById(R.id.myaccount_fullname_tv);
        checkUser();

        newRecipe = view.findViewById(R.id.myaccount_addrecipe_btn);
        newRecipe.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionGlobalNewRecipeFragment(viewModel.getCurrentUser()));

        });

        Button myRecipes = view.findViewById(R.id.myaccount_myrecipes_btn);
        myRecipes.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment(viewModel.getCurrentUser(), ""));

        });
        Button othersRecipes = view.findViewById(R.id.myaccount_othersrecipes_btn);
        othersRecipes.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToRecipesListFragment("",""));
        });

        Button editMyAccount = view.findViewById(R.id.myaccount_edit_btn);
        editMyAccount.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).navigate(MyAccountFragmentDirections.actionMyAccountNavToEditMyAccountFragment(fullNameAsId));

        });

        logOutBtn = view.findViewById(R.id.myAccount_logout_btn);
        logOutBtn.setOnClickListener((v)->{
            currentUserEmail = viewModel.getCurrentUserEmail();
            viewModel.signOut();
            checkUser();
        });

        setHasOptionsMenu(true);

        return view;


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item =menu.findItem(R.id.menu_myAccount);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = viewModel.getCurrentUserEmail();
            viewModel.signOut();
            viewModel.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });

            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void checkUser() {
        if(Model.instance.getCurrentUser() == null){
            viewModel.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });

        }else{
            String userName = viewModel.getCurrentUserFullName();
            if (userName == null || userName.equals("")) {
                userName = viewModel.getCurrentUserUsername();
            }
            fullName.setText(userName);
            fullNameAsId = userName;
        }
    }



}