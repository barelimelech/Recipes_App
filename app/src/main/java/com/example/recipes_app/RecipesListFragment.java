package com.example.recipes_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recipes_app.model.Recipe;

import java.util.List;


public class RecipesListFragment extends Fragment {
    List<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_categories_list,container,false);
//        recipes = Model.instance.getAllRecipes();
//
//
//        RecyclerView list = view.findViewById(R.id.categorieslist_rv);
//        list.setHasFixedSize(true);
//
//        list.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        RecipesListFragment.MyAdapter adapter = new RecipesListFragment.MyAdapter();
//        list.setAdapter(adapter);
////
////        adapter.setOnItemClickListener(new CategoriesListFragment.OnItemClickListener() {
////            @Override
////            public void onItemClick(int position) {
////                String stId = recipes.get(position);
////
////            }
////        });
//
//
//        setHasOptionsMenu(true);
//        return view;

        return inflater.inflate(R.layout.fragment_categories_list,container,false);

    }

//
//    class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView nameTv;
//        TextView idTv;
//        CheckBox cb;
//
//        public MyViewHolder(@NonNull View itemView, CategoriesListFragment.OnItemClickListener listener) {
//            super(itemView);
//            nameTv = itemView.findViewById(R.id.category_listrow_name);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    listener.onItemClick(pos);
//
//                }
//            });
//        }
//    }
//
//    interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//    class MyAdapter extends RecyclerView.Adapter<RecipesListFragment.MyViewHolder>{
//
//        RecipesListFragment.OnItemClickListener listener;
//
//        public void setOnItemClickListener(RecipesListFragment.OnItemClickListener listener){
//            this.listener = listener;
//        }
//
//        @NonNull
//        @Override
//        public RecipesListFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = getLayoutInflater().inflate(R.layout.fragment_recipe_row,parent,false);
//            RecipesListFragment.MyViewHolder holder = new RecipesListFragment.MyViewHolder(view,listener);
//            return holder;
//
//
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecipesListFragment.MyViewHolder holder, int position) {
//            //String recipe = recipes.get(position);
//          ///  holder.nameTv.setText(recipe);
//
//        }
//
//        @Override
//        public int getItemCount() {
//            if(recipes == null) {
//                return 0;
//            }
//            return recipes.size();
//
//        }
//    }
}