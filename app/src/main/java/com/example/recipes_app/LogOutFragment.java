//package com.example.recipes_app;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.recipes_app.databinding.FragmentLogInBinding;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LogOutFragment extends Fragment {
//
//
//    private FragmentLogInBinding binding;
//    private FirebaseAuth firebaseAuth;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_log_out, container, false);
//
//        binding = FragmentLogInBinding.inflate(getLayoutInflater());
//        firebaseAuth= FirebaseAuth.getInstance();
//
//        Button logOutBtn = view.findViewById(R.id.logout_btn);
//
//        logOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
//                //checkUser();
////                FirebaseAuth.getInstance().signOut();
////                Intent intent = new Intent(getApplicationContext(), Profile.class);
////                startActivity(intent);
//            }
//        });
//       // checkUser();
//
//
//        return view;
//    }
//
//
////    private void checkUser() {
////        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
////        if(firebaseUser == null){
////            startActivity(new Intent(this,MainActivity.class));
////            finish();
////        }else{
////            String email=firebaseUser.getEmail();
////            binding.email.setText(email);
////        }
////    }
//}