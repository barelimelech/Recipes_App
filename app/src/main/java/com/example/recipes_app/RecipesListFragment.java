package com.example.recipes_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.List;


public class RecipesListFragment extends Fragment {
    RecipesListViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipesListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        swipeRefresh = view.findViewById(R.id.recipeslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshRecipeList());

        // String recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();

        RecyclerView list = view.findViewById(R.id.recipeslist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String recipeId = viewModel.getRecipes().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId));
            }
        });


        Button addRecipe = view.findViewById(R.id.myaccount_add_btn);
        addRecipe.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
        });


        setHasOptionsMenu(true);
        //when data update
        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getRecipeListLoadingState().getValue() == Model.RecipeListLoadingState.loading);
        Model.instance.getRecipeListLoadingState().observe(getViewLifecycleOwner(), recipeListLoadingState -> {
            if (recipeListLoadingState == Model.RecipeListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });
        return view;

    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
//        swipeRefresh.setRefreshing(true);
//        Model.instance.getAllRecipes((list)->{
//            viewModel.setRecipes(list);
//            adapter.notifyDataSetChanged();
//            swipeRefresh.setRefreshing(false);
//        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;


        public MyViewHolder(@NonNull View itemView, RecipesListFragment.OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.recipe_listrow_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);

                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    class MyAdapter extends RecyclerView.Adapter<RecipesListFragment.MyViewHolder> {

        RecipesListFragment.OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecipesListFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recipe_list_row, parent, false);
            RecipesListFragment.MyViewHolder holder = new RecipesListFragment.MyViewHolder(view, listener);
            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull RecipesListFragment.MyViewHolder holder, int position) {
            Recipe recipe = viewModel.getRecipes().getValue().get(position);
            Log.d("TAG", "the recipe is  : : : : " + recipe);
            Log.d("TAG", "the recipe name is  : : : : " + recipe.getName());

            holder.nameTv.setText(recipe.getName());

        }

        @Override
        public int getItemCount() {
            if (viewModel.getRecipes().getValue() == null) {
                return 0;
            }
            return viewModel.getRecipes().getValue().size();

        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionGlobalMyAccountFragment());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}