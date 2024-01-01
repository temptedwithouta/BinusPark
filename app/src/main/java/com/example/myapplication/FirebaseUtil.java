package com.example.myapplication;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

public class FirebaseUtil {

    public static DatabaseReference usersReference() {
        return FirebaseDatabase.getInstance().getReference().child("users");
    }

    public interface UserDetailListener {
        void onUserDetail(DataSnapshot dataSnapshot);
        void onError(DatabaseError databaseError);
        void onUserNotFound();
    }

    public static void currentUserDetails(String userEmail, FirebaseUtil.UserDetailListener listener) {
        DatabaseReference usersRef = usersReference();
        Query query = usersRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (listener != null) {
                            listener.onUserDetail(userSnapshot);
                            return; // Stop after finding the user
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onUserNotFound();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onError(databaseError);
                }
            }
        });
    }
}
