package com.example.recipes_app.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recipes_app.R;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class MyAccountFragment extends Fragment {
    List<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);

//        recipes = Model.instance.getAllRecipes();
//
//
//        RecyclerView list = view.findViewById(R.id.categorieslist_rv);
//        list.setHasFixedSize(true);
//
//        list.setLayoutManager(new LinearLayoutManager(getContext()));
//
////        MyAccountFragment.MyAdapter adapter = new MyAccountFragment.MyAdapter();
////        list.setAdapter(adapter);
////
//////        adapter.setOnItemClickListener(new MyAccountFragment.OnItemClickListener() {
//////            @Override
//////            public void onItemClick(int position) {
//////                String stId = recipes.get(position);
//////
//////            }
//////        });
////
////
////
////        setHasOptionsMenu(true);
//        return view;
    }


//    class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView nameTv;
//        TextView idTv;
//        CheckBox cb;
//
//        public MyViewHolder(@NonNull View itemView, MyAccountFragment.OnItemClickListener listener) {
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
//    class MyAdapter extends RecyclerView.Adapter<MyAccountFragment.MyViewHolder>{
//
//        MyAccountFragment.OnItemClickListener listener;
//
//        public void setOnItemClickListener(MyAccountFragment.OnItemClickListener listener){
//            this.listener = listener;
//        }
//
//        @NonNull
//        @Override
//        public MyAccountFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = getLayoutInflater().inflate(R.layout.categories_list_row,parent,false);
//            MyAccountFragment.MyViewHolder holder = new MyAccountFragment.MyViewHolder(view,listener);
//            return holder;
//
//
//        }
//
////        @Override
////        public void onBindViewHolder(@NonNull MyAccountFragment.MyViewHolder holder, int position) {
////            String category = recipes.get(position);
////            holder.nameTv.setText(category);
////
////        }
//
//        @Override
//        public int getItemCount() {
//            if(recipes == null) {
//                return 0;
//            }
//            return recipes.size();
//
//        }
   // }
}