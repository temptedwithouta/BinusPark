package com.example.myapplication;
//Initial library imports
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

//Helps firebase related function for ProfileActivity
public class FirebaseUtil {

    //Connects to the needed table
    public static DatabaseReference usersReference() {
        return FirebaseDatabase.getInstance().getReference().child("users");
    }

    //Interface to handle snapshot and errors
    public interface UserDetailListener {
        void onUserDetail(DataSnapshot dataSnapshot);
        void onError(DatabaseError databaseError);
        void onUserNotFound();
    }

    //Returns the currentUserDetails
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
                            return;
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