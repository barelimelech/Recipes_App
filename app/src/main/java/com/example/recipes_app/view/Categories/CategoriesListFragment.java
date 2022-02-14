//package com.example.recipes_app.view.Categories;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.recipes_app.R;
//import com.example.recipes_app.model.Model;
//
//import java.util.List;
//
//public class CategoriesListFragment extends Fragment {
//
//    String usernameAsId;
//    //private FirebaseFirestore db;
//    List<String> categories;
//    TextView headline;
//
//    CategoriesListViewModel viewModel;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        viewModel = new ViewModelProvider(this).get(CategoriesListViewModel.class);
//
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_categories_list,container,false);
//        categories = viewModel.getAllCategories();
//        usernameAsId = CategoriesListFragmentArgs.fromBundle(getArguments()).getUsername();
//        headline = view.findViewById(R.id.categorieslist_headline_tv);
//        headline.setText("Welcome " +usernameAsId+" :)"+"\n"+"Please choose category:");
//
//        //db = FirebaseFirestore.getInstance();
//
//
//        RecyclerView list = view.findViewById(R.id.categorieslist_rv);
//        list.setHasFixedSize(true);
//
//        list.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        MyAdapter adapter = new MyAdapter();
//        list.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String stId = categories.get(position);
//
//            }
//        });
//
////        Model.instance.getUserByUsername(usernameAsId, new Model.GetUserByUsername() {
////
////            @Override
////            public void onComplete(User user) {
////                headline.setText("Welcome " +user.getUsername()+" :)"+"\n"+"Please choose category:");
////            }
////        });
//
//
//
//        setHasOptionsMenu(true);
//        return view;
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView nameTv;
//        ImageView categoryImage;
//
//
//
//        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
//            super(itemView);
//            nameTv = itemView.findViewById(R.id.category_listrow_name);
//           // categoryImage = itemView.findViewById(R.id.category_listrow_image);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    listener.onItemClick(pos);
//
//                    NavHostFragment.findNavController(CategoriesListFragment.this)
//                            .navigate(CategoriesListFragmentDirections
//                                    .actionCategoriesListFragmentToRecipesListFragment(usernameAsId,categories.get(pos)));
//
//
//                }
//            });
//        }
//    }
//
//    interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
//
//        OnItemClickListener listener;
//
//        public void setOnItemClickListener(OnItemClickListener listener){
//            this.listener = listener;
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = getLayoutInflater().inflate(R.layout.categories_list_row,parent,false);
//            MyViewHolder holder = new MyViewHolder(view,listener);
//            return holder;
//
//
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//            String category = categories.get(position);
//            holder.nameTv.setText(category);
//            //holder.categoryImage.setImageResource(R.drawable.breakfast);
//
//        }
//
//        @Override
//        public int getItemCount() {
//            if(categories == null) {
//                return 0;
//            }
//            return categories.size();
//
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.menu_myAccount){
//            Log.d("TAG","ADD...");
//           NavHostFragment.findNavController(this).navigate(CategoriesListFragmentDirections.actionGlobalMyAccountFragment(Model.instance.getCurrentUsername()));
//            return true;
//        }else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        MenuItem item =menu.findItem(R.id.menu_myAccount);
//        item.setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }
//
//}