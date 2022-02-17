package com.example.recipes_app.view.RecipesList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipes_app.LoginActivity;
import com.example.recipes_app.R;
import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.view.MyAccount.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecipesListFragment extends Fragment {
    RecipesListViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    List<Recipe> recipes = new ArrayList<>();
    String fullNameAsId;
    String fullNameAsIdnew;
    String category;

    UserViewModel userViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipesListViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        fullNameAsId = RecipesListFragmentArgs.fromBundle(getArguments()).getUsername();
        category = RecipesListFragmentArgs.fromBundle(getArguments()).getCategory();

        swipeRefresh = view.findViewById(R.id.recipeslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refreshList());

        RecyclerView list = view.findViewById(R.id.recipeslist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position, viewId) -> {
            String id = viewModel.recipes.getValue().get(position).getId();
            String url = viewModel.recipes.getValue().get(position).getRecipeUrl();
            String username = viewModel.recipes.getValue().get(position).getUsername();
            String type = viewModel.recipes.getValue().get(position).getType();

            if(url==null){
                url = "0";
            }
            if(v.findViewById(R.id.recipe_listrow_delete).getId()==viewId){
                viewModel.deleteRecipe(viewModel.getRecipes().getValue().get(position),()->{
                    refreshList();
                });
            }else if(v.findViewById(R.id.recipe_listrow_edit).getId()==viewId){
                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToEditRecipeFragment(id,username,type));
            }else{
                String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId,fullNameAsId));
            }
        });

        viewModel.refreshUserRecipesList();
        setHasOptionsMenu(true);

        if(fullNameAsId.equals("")) {
            //when data update
            viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> refresh());
            swipeRefresh.setRefreshing(ModelRecipe.instance.getRecipeListLoadingState().getValue() == ModelRecipe.RecipeListLoadingState.loading);
            ModelRecipe.instance.getRecipeListLoadingState().observe(getViewLifecycleOwner(), recipeListLoadingState -> {
                if (recipeListLoadingState == ModelRecipe.RecipeListLoadingState.loading) {
                    swipeRefresh.setRefreshing(true);
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            });
        }else{
        viewModel.refreshUserRecipesList();
        viewModel.getRecipesByUsername().observe(getViewLifecycleOwner(), recipes -> refresh());
            swipeRefresh.setRefreshing(ModelRecipe.instance.getRecipeListLoadingState().getValue() == ModelRecipe.RecipeListLoadingState.loading);
            ModelRecipe.instance.getRecipeListLoadingState().observe(getViewLifecycleOwner(), recipeListLoadingState -> {
                if (recipeListLoadingState == ModelRecipe.RecipeListLoadingState.loading) {
                    swipeRefresh.setRefreshing(true);
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            });
        }
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    private void refreshList(){
        if(fullNameAsId.equals("")) {
            viewModel.refreshRecipesList();
        }
        else{
            viewModel.refreshUserRecipesList();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView usernameBy;
        ImageButton deleteBtn, editBtn;
        ImageView recipeImage;



        public MyViewHolder(@NonNull View itemView, RecipesListFragment.OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.recipe_listrow_name);
            usernameBy = itemView.findViewById(R.id.recipe_listrow_username);
            recipeImage = itemView.findViewById(R.id.recipe_listrow_image);
            deleteBtn = itemView.findViewById(R.id.recipe_listrow_delete);
            editBtn = itemView.findViewById(R.id.recipe_listrow_edit);

            String userName = viewModel.getCurrentUser();
            if (userName == null || userName.equals("")) {
                userName = viewModel.getCurrentUser();
            }
            fullNameAsIdnew = userName;

            itemView.setOnClickListener(v -> {
                int viewId = v.getId();

                int pos = getAdapterPosition();
                listener.onItemClick(v,pos,viewId);

            });
            deleteBtn.setOnClickListener((v)->{
                int viewId = v.getId();
                int position = getAdapterPosition();
                listener.onItemClick(itemView,position,viewId);
            });

            editBtn.setOnClickListener((v)->{
                int viewId = v.getId();
                int position = getAdapterPosition();
                listener.onItemClick(itemView,position,viewId);
            });


        }
        void bind(Recipe recipe){
            recipeImage.setImageResource(R.drawable.cake);
            nameTv.setText(recipe.getName());
            usernameBy.setText("By:  "+recipe.getUsername());

            if (recipe.getRecipeUrl() != null) {
                Picasso.get()
                        .load(recipe.getRecipeUrl())
                        .into(recipeImage);
            }

            if(!fullNameAsId.equals("")&& recipe.getUsername() != null){
                    deleteBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
            } else{

                if(!recipe.getUsername().equals(viewModel.getCurrentUser())){
                    deleteBtn.setVisibility(View.GONE);
                    editBtn.setVisibility(View.GONE);
                }
                else{
                    deleteBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position, int viewId);
    }
    class MyAdapter extends RecyclerView.Adapter<RecipesListFragment.MyViewHolder>{

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;

        }


        @NonNull
        @Override
        public RecipesListFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recipe_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull RecipesListFragment.MyViewHolder holder, int position) {
            //Recipe recipe = viewModel.getRecipes().getValue().get(position);
            Recipe recipe;
            if(fullNameAsId.equals("") || !category.equals("")) {
                recipe = viewModel.getRecipes().getValue().get(position);
            }
            else{
                recipe = viewModel.getRecipesByUsername().getValue().get(position);
            }
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getRecipes().getValue() == null) {
                return 0;
            }else if(fullNameAsId.equals("")|| !category.equals("")) {
                return viewModel.getRecipes().getValue().size();
            }
            else{
                return viewModel.getRecipesByUsername().getValue().size();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionGlobalMyAccountFragment(viewModel.getCurrentUser()));
            return true;
        }else if(item.getItemId() == R.id.logout_menu){
            String currentUserEmail = userViewModel.getCurrentUserEmail();
            userViewModel.signOut();
            userViewModel.logout(currentUserEmail, () -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            });
            return true;
        }
        else if(item.getItemId() == R.id.newRecipeFragment_menu){
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToNewRecipeFragment(viewModel.getCurrentUser()));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.myaccount_menu, menu);
        inflater.inflate(R.menu.add_recipe_menu, menu);
    }
}
