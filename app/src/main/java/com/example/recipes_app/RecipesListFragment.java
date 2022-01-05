package com.example.recipes_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
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

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;


public class RecipesListFragment extends Fragment {
    RecipesListViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    List<Recipe> recipes = new ArrayList<>();
    //RecipesListViewModel tmpRecipes;
    String usernameAsId;
    String category;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipesListViewModel.class);
        //tmpRecipes= new ViewModelProvider(this).get(RecipesListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        usernameAsId = RecipesListFragmentArgs.fromBundle(getArguments()).getUsername();//TODO:show recipes cy id
        category = RecipesListFragmentArgs.fromBundle(getArguments()).getCategory();



        swipeRefresh = view.findViewById(R.id.recipeslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshRecipeList());
        Log.d("TAG", "recipes  : : : : " + recipes);

        // String recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeId();

        RecyclerView list = view.findViewById(R.id.recipeslist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getName();
                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId));
            }
        });


        Button addRecipe = view.findViewById(R.id.myaccount_add_btn);
        addRecipe.setOnClickListener((v)->{
            //Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
            Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToNewRecipeFragment(usernameAsId));//the user that make the new recipe

        });


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

//        if(usernameAsId!=null){
//            viewModel.getRecipesOfUser(usernameAsId).observe(getViewLifecycleOwner(), recipes -> refresh());
//            swipeRefresh.setRefreshing(Model.instance.getRecipeListLoadingState().getValue() == Model.RecipeListLoadingState.loading);
//            Model.instance.getRecipeListLoadingState().observe(getViewLifecycleOwner(), recipeListLoadingState -> {
//                if (recipeListLoadingState == Model.RecipeListLoadingState.loading) {
//                    swipeRefresh.setRefreshing(true);
//                } else {
//                    swipeRefresh.setRefreshing(false);
//                }
//            });        }

//        if(category!=null){
//            viewModel.getRecipesOfCategory(category).observe(getViewLifecycleOwner(), recipes -> refresh());
//            swipeRefresh.setRefreshing(Model.instance.getRecipeListLoadingState().getValue() == Model.RecipeListLoadingState.loading);
//            Model.instance.getRecipeListLoadingState().observe(getViewLifecycleOwner(), recipeListLoadingState -> {
//                if (recipeListLoadingState == Model.RecipeListLoadingState.loading) {
//                    swipeRefresh.setRefreshing(true);
//                } else {
//                    swipeRefresh.setRefreshing(false);
//                }
//            });
//        }
        //refresh();
        setHasOptionsMenu(true);

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
    class MyAdapter extends RecyclerView.Adapter<RecipesListFragment.MyViewHolder> implements Filterable {

        RecipesListFragment.OnItemClickListener listener;
       // List<Recipe> tmpList = viewModel.getRecipes().getValue();

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;

        }

        @NonNull
        @Override
        public RecipesListFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recipe_list_row, parent, false);
            RecipesListFragment.MyViewHolder holder = new RecipesListFragment.MyViewHolder(view, listener);
            //recipes.addAll(viewModel.recipes.getValue());
           // Log.d("TAG", "recipesssssssssssssssssssssssssssssssssssss " + recipes);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull RecipesListFragment.MyViewHolder holder, int position) {
            Recipe recipe = viewModel.getRecipes().getValue().get(position);
            Log.d("TAG", "the recipe is  : : : : " + recipe);
            Log.d("TAG", "the recipe name is  : : : : " + recipe.getName());
            //tmpList = viewModel.getRecipes().getValue();
            holder.nameTv.setText(recipe.getName());

        }

        @Override
        public int getItemCount() {
            if (viewModel.getRecipes().getValue() == null) {
                return 0;
            }
            //tmpList = viewModel.getRecipes().getValue();

            return viewModel.getRecipes().getValue().size();

        }
        @Override
        public Filter getFilter() {
            return myFilter;
        }

        private Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Recipe> list = new ArrayList<>();
                if(constraint == null || constraint.length() == 0){
                    //List<Recipe> tmpList = viewModel.getRecipes().getValue();

                    list.addAll(recipes);
                }else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Recipe item : viewModel.recipes.getValue()) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            list.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = list;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Recipe> list1 = viewModel.getRecipes().getValue();

                list1.clear();
                list1.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
//        public void updateList(ArrayList<Recipe> newList) {
//            Log.i("String:", newList.toString());
//
//           // viewModel.recipes.getValue() = new ArrayList<>();
//            viewModel.recipes.getValue().addAll(newList);
//
//
//            notifyDataSetChanged();
//        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_myAccount) {
            Log.d("TAG", "ADD...");
            NavHostFragment.findNavController(this).navigate(RecipesListFragmentDirections.actionGlobalMyAccountFragment(usernameAsId));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_menu, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search_bar_menu);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query)
            {

//                if (recipes.contains(query)) {
//                    adapter.getFilter().filter(query);
//                }
//                else {
//                    // Search query not found in List View
//                    Toast.makeText(RecipesListFragment.this, "Not found", Toast.LENGTH_LONG).show();
//                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);

                if(newText.length()==0){
                    //list.addAll(viewModel.recipes.getValue());

                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }
}


