package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


public class ProfileFragment extends Fragment{
    ImageButton backBtn;
    EditText nameHere;
    EditText emailHere;
    EditText phoneHere;
    EditText passwordHere;
    Button updateProfileBtn;
    TextView keluar;

    UserModel currentUserModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        nameHere = view.findViewById(R.id.full_name);
        emailHere = view.findViewById(R.id.email);
        phoneHere = view.findViewById(R.id.phone_number);
        passwordHere = view.findViewById(R.id.password);
        backBtn = view.findViewById(R.id.back);
        updateProfileBtn = view.findViewById(R.id.profile_update);
        keluar = view.findViewById(R.id.logout_button);
        // Retrieve the Intent from the hosting Activity
        Intent receivedIntent = getActivity().getIntent();

        if (receivedIntent != null && receivedIntent.hasExtra("email")) {
            String emails = receivedIntent.getStringExtra("email");
            // Use the retrieved value as needed
            getUserData(emails);
        }

        updateProfileBtn.setOnClickListener(v->{
            if (receivedIntent != null && receivedIntent.hasExtra("email")) {
                String emails = receivedIntent.getStringExtra("email");
                // Use the retrieved value as needed
                editBtnClick(emails);
            }
        });

        return view;

    }


    void editBtnClick(String email) {
        String newFullName = nameHere.getText().toString();
        if (newFullName.isEmpty() || newFullName.length() < 2) {
            nameHere.setError("Full name must be at least 2 characters");
            return;
        } else {
            updateToFirebase(email, newFullName);
        }
    }


    void updateToFirebase(String email, String name) {
        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
            @Override
            public void onUserDetail(DataSnapshot dataSnapshot) {
                // Update name in the database for the user with the provided email
                DatabaseReference userRef = dataSnapshot.getRef();
                userRef.child("name").setValue(name)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                               showToast(getContext(), "Updated successfully");
                            } else {
                                showToast(getContext(), "Edit failed");
                            }
                        });
            }

            @Override
            public void onError(DatabaseError databaseError) {
                // Handle error while retrieving user details
            }

            @Override
            public void onUserNotFound() {
                // Handle case when user data is not found
            }
        });
    }

    void getUserData(String email) {
        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
            @Override
            public void onUserDetail(DataSnapshot dataSnapshot) {
                // Handle user details fetched from Realtime Database
                String name = dataSnapshot.child("name").getValue(String.class);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                if (name != null && email != null && phone != null) {
                    nameHere.setText(name);
                    emailHere.setText(userEmail);
                    phoneHere.setText(phone);
                }
            }

            @Override
            public void onError(DatabaseError databaseError) {
                // Handle error while retrieving user details from Realtime Database
            }

            @Override
            public void onUserNotFound() {
                // Handle case when user data is not found in Realtime Database
            }
        });
    }
    public static  void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }



}