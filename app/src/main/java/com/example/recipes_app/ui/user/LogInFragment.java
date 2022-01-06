package com.example.recipes_app.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LogInFragment extends Fragment {

    EditText username;
    EditText password;
    String usernameAsId;

    UsersListViewModel viewModel;


    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private int permission =0;


    //LogInFragment.MyAdapter adapter;
    //SwipeRefreshLayout swipeRefresh;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UsersListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);




//        SignInButton authButton = view.findViewById(R.id.google_signin);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("211609576402-f9ugjpkpljeton1bre8gr98hfj9icerq.apps.googleusercontent.com").requestEmail().build();
//        mAuth = FirebaseAuth.getInstance();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        authButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });




        //username = view.findViewById(R.id.login_username_tv);
        //password = view.findViewById(R.id.lodin_password_tv);
        //recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();
        //swipeRefresh = view.findViewById(R.id.recipeslist_swiperefresh);
        //swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshUserList());

        //Button signUp = view.findViewById(R.id.login_signup_btn);
//        signUp.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_signUpFragment);
//        });
//
//        Button logIn = view.findViewById(R.id.login_login_btn);
//        logIn.setOnClickListener((v)->{
//            boolean bool = save();
//            //Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_categoriesListFragment);
//            if(bool) {
//                NavHostFragment.findNavController(this).navigate(LogInFragmentDirections.actionNavHomeToCategoriesListFragment2(usernameAsId));
//            }
//           // Navigation.findNavController(v).navigate(R.id.action_nav_home_to_categoriesListFragment2);
//        });


        //viewModel.getUsers().observe(getViewLifecycleOwner(),users  -> refresh());
//        swipeRefresh.setRefreshing(Model.instance.getUserListLoadingState().getValue() == Model.UserListLoadingState.loading);
//        Model.instance.getUserListLoadingState().observe(getViewLifecycleOwner(), userListLoadingState -> {
//            if (userListLoadingState == Model.UserListLoadingState.loading) {
//                swipeRefresh.setRefreshing(true);
//            } else {
//                swipeRefresh.setRefreshing(false);
//            }
//        });


        return view;
    }

    private boolean save() {
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        usernameAsId = username1;

        if (TextUtils.isEmpty(username1)){
            username.setError("Please Enter username!");
        }
        else if(TextUtils.isEmpty(password1)){
            password.setError("Please Enter password!");
        }
        else {
            List<User> users = viewModel.getUsers().getValue();
            boolean flag = false;
            for(User u : users) {
                if (u.getUsername().equals(username1)){
                    if (!password1.equals(u.getPassword())) {
                        password.setError("password incorrect!");
                        return false;
                    }
                    flag = true;
                }
            }
            if(!flag){
                username.setError("User does not exist!");
                return false;
            }

            Model.instance.getUserByUsername(username1, new Model.GetUserByUsername() {

                @Override
                public void onComplete(User user) {
                    Log.d("TAG", "hello " + username1);
                    user.setUsername(username1);
                    user.setPassword(password1);
                }
            });
            return true;
        }
        return false;
       // NavHostFragment.findNavController(this).navigate(EditMyAccountFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));
    }

}