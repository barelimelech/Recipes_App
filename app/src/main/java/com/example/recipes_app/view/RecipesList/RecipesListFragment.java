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
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.recipes_app.databinding.FragmentRecipeBinding;
import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.view.MyAccount.UsersListViewModel;
import com.google.firebase.auth.FirebaseAuth;
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
    ImageButton deleteRecipe;

    private FirebaseAuth firebaseAuth;
    UsersListViewModel usersListViewModel;

    private FragmentRecipeBinding binding;

//    public interface OnDeleteClickListener{
//        void OnDeleteClickListener(Recipe recipe);
//    }
//
//
//    private OnDeleteClickListener onDeleteClickListener;

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
        String name = Model.instance.getCurrentUserFullName();
//        String name = Model.instance.getCurrentUsername();
        fullNameAsId = RecipesListFragmentArgs.fromBundle(getArguments()).getUsername();
        category = RecipesListFragmentArgs.fromBundle(getArguments()).getCategory();
        //name = Model.instance.getCurrentUsername();
//        if (name == null || name.equals("")) {
//            name = Model.instance.getCurrentUsername();
//        }
        //fullNameAsId = name;
        Log.d("TAG", "lllllll " + fullNameAsId);
        binding = FragmentRecipeBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();

        //deleteRecipe = view.findViewById(R.id.recipe_listrow_delete);
//        viewModel.deleteRecipe(recipe, ()->{
//            Model.instance.refreshRecipeList();
//        });

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
            public void onItemClick(View v, int position, int viewId) {
                String id = viewModel.recipes.getValue().get(position).getId();
                String name = viewModel.recipes.getValue().get(position).getName();
                String method = viewModel.recipes.getValue().get(position).getMethod();
                String ingredients = viewModel.recipes.getValue().get(position).getIngredients();
                String url = viewModel.recipes.getValue().get(position).getRecipeUrl();
                String username = viewModel.recipes.getValue().get(position).getUsername();
                String type = viewModel.recipes.getValue().get(position).getType();
                //String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getId();
                //Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId,fullNameAsId));
                if(url==null){
                    url = "0";
                }
                if(v.findViewById(R.id.recipe_listrow_delete).getId()==viewId){
                    viewModel.deleteRecipe(viewModel.getRecipes().getValue().get(position),()->{
                        Model.instance.refreshRecipeList();
                    });
                }else if(v.findViewById(R.id.recipe_listrow_edit).getId()==viewId){
                    Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToEditRecipeFragment(id,username));

                }else{
                    String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getId();
                    Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId,fullNameAsId));
                }
            }



//            @Override
//            public void onItemClick(View v, int position, int viewId) {
//
//
////                String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getName();
////                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId));
//                String recipeNameAsId = viewModel.getRecipes().getValue().get(position).getId();
//                Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeNameAsId,fullNameAsId));
//            }
        });


//        Button addRecipe = view.findViewById(R.id.myaccount_add_btn);
//        addRecipe.setOnClickListener((v)->{
//            //Navigation.findNavController(v).navigate(R.id.action_global_newRecipeFragment);
//            Navigation.findNavController(v).navigate(RecipesListFragmentDirections.actionRecipesListFragmentToNewRecipeFragment(fullNameAsId));//the user that make the new recipe
//
//        });

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

        //refresh();

        return view;

    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        // swipeRefresh.setRefreshing(false);
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView usernameBy;
        ImageButton deleteBtn, editBtn;
        ImageView recipeImage;
        Recipe tmp;



        public MyViewHolder(@NonNull View itemView, RecipesListFragment.OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.recipe_listrow_name);
            usernameBy = itemView.findViewById(R.id.recipe_listrow_username);
            recipeImage = itemView.findViewById(R.id.recipe_listrow_image);
            deleteBtn = itemView.findViewById(R.id.recipe_listrow_delete);
            editBtn = itemView.findViewById(R.id.recipe_listrow_edit);
            //  deleteBtn.setOnClickListener(this);

//            if(!fullNameAsId.equals("")){
//
//                itemView.setVisibility(View.GONE);
//            }
            String userName = Model.instance.getCurrentUsername();
            if (userName == null || userName.equals("")) {
                userName = Model.instance.getCurrentUsername();
            }
            fullNameAsIdnew = userName;
            Log.d("TAG", "fullNameAsId " +  fullNameAsId );


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = v.getId();

                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos,viewId);

                }
            });
            deleteBtn.setOnClickListener((v)->{
                int viewId = v.getId();
                int position = getAdapterPosition();
                listener.onItemClick(itemView,position,viewId);


//                Model.instance.deleteRecipe(tmp,()->{
//                    Model.instance.refreshRecipeList();
//                    //Navigation.findNavController(itemView).navigateUp();
//                    //NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalRecipesListFragment(usernameAsId,recipeNameAsId));
//                    //NavHostFragment.findNavController(this).navigate(EditRecipeFragmentDirections.actionGlobalMyAccountFragment(recipe.getUsername()));//TODO:!!!!
//                });
            });

            editBtn.setOnClickListener((v)->{
                int viewId = v.getId();
                int position = getAdapterPosition();
                listener.onItemClick(itemView,position,viewId);
            });


        }
        void bind(Recipe recipe){
//            nameTv.setText(recipe.getName());
//            usernameBy.setText("By:  "+recipe.getUsername());
//            if (recipe.getRecipeUrl() != null) {
//                Picasso.get()
//                        .load(recipe.getRecipeUrl())
//                        .into(recipeImage);
//            }
            recipeImage.setImageResource(R.drawable.cake);


            if(!fullNameAsId.equals("")&& recipe.getUsername() != null&&category.equals("")){
                if(!recipe.getUsername().equals(fullNameAsId)){
                    itemView.setVisibility(View.GONE);
                    itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));


                }
                else{
                    nameTv.setText(recipe.getName());
                    usernameBy.setText("By:  "+recipe.getUsername());
                    // recipeImage.setImageResource(R.drawable.cake);
                    // binding.recipeDetailsEditBtn.findViewById(R.id.recipeDetails_edit_btn).setVisibility(View.GONE);
                    if (recipe.getRecipeUrl() != null) {
                        Picasso.get()
                                .load(recipe.getRecipeUrl())
                                .into(recipeImage);
                    }
                    deleteBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
//                    else
//                        recipeImage.setImageBitmap(null);

                }
            }else if(!category.equals("") && recipe.getType() != null){
                if(!recipe.getType().equals(category)){
                    itemView.setVisibility(View.GONE);
                    itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                else{
                    nameTv.setText(recipe.getName());
                    usernameBy.setText("By:  "+recipe.getUsername());
                    //recipeImage.setImageResource(R.drawable.cake);
                    if (recipe.getRecipeUrl() != null) {
                        Picasso.get()
                                .load(recipe.getRecipeUrl())
                                .into(recipeImage);
                    }
//                    else{
//                        recipeImage.setImageResource(R.drawable.empty);
////                        recipeImage.setImageBitmap(null);
//                    }
                }
            } else{

                nameTv.setText(recipe.getName());
                usernameBy.setText("By:  " + recipe.getUsername());
                // binding.recipeDetailsEditBtn.findViewById(R.id.recipeDetails_edit_btn).setVisibility(View.GONE);

                if (recipe.getRecipeUrl() != null) {
                    Picasso.get()
                            .load(recipe.getRecipeUrl())
                            .into(recipeImage);
                }
//                else{
//                    recipeImage.setImageResource(R.drawable.empty);
//                    recipeImage.setImageBitmap(null);
//                }
//                if(!recipe.getUsername().equals(firebaseUser.getDisplayName())){
//                if(!recipe.getUsername().equals(fullNameAsIdnew)){
                if(!recipe.getUsername().equals(Model.instance.getCurrentUsername())){
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
    class MyAdapter extends RecyclerView.Adapter<RecipesListFragment.MyViewHolder> implements Filterable {

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
            Recipe recipe = viewModel.getRecipes().getValue().get(position);
            //holder.tmp = recipe;
            //holder.nameTv.setText(recipe.getName());

            holder.bind(recipe);

        }

        @Override
        public int getItemCount() {
            if (viewModel.getRecipes().getValue() == null) {
                return 0;
            }
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
//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        MenuItem item =menu.findItem(R.id.menu_myAccount);
//        item.setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }
//@Override
//public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//    super.onCreateOptionsMenu(menu, inflater);
//    inflater.inflate(R.menu.search_menu,menu);
//}


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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.myaccount_menu, menu);
        inflater.inflate(R.menu.add_recipe_menu, menu);


//        MenuItem searchViewItem = menu.findItem(R.id.search_bar_menu);
//        SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//
////                if (recipes.contains(query)) {
////                    adapter.getFilter().filter(query);
////                }
////                else {
////                    // Search query not found in List View
////                    Toast.makeText(RecipesListFragment.this, "Not found", Toast.LENGTH_LONG).show();
////                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                adapter.getFilter().filter(newText);
//
//                if(newText.length()==0){
//                    //list.addAll(viewModel.recipes.getValue());
//
//                }
//                return false;
//            }
//        });

    }
}
