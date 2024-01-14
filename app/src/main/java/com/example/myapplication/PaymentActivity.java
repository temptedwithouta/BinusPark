package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.Reservation;
import com.example.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity
{
  private TextView locationTextView, priceTextView;
  private ImageView qrCode;
  private double price = 0;
  private String location;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    locationTextView = findViewById(R.id.location);
    priceTextView = findViewById(R.id.price);
    qrCode = findViewById(R.id.QRCode);

    location = getIntent().getExtras().getString("location");
    locationTextView.setText(location);
    price = getIntent().getExtras().getDouble("price");
    priceTextView.setText("Total Fee: "+PriceFormat(price));

    cookiesDb dbHandler;

    dbHandler = new cookiesDb(this);

    qrCode.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {

        DatabaseReference dbReference;

        dbReference = FirebaseDatabase.getInstance().getReference("reserves");

//        dbReference.addValueEventListener(new ValueEventListener() {
//          @Override
//          public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
//            for(DataSnapshot child : dataSnapShot.getChildren()){
//              String location = child.child("universityName").getValue(String.class);
//
//              String username = child.child("username").getValue(String.class);
//            }
//          }
//
//          @Override
//          public void onCancelled(@NonNull DatabaseError error) {
//          }
//        });

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            String location2 = getIntent().getExtras().getString("location");

            ArrayList<User> userCookies = dbHandler.getUserCookies();

            User user = userCookies.get(0);

            for(DataSnapshot child : snapshot.getChildren()){
              String location = child.child("universityName").getValue(String.class);

              String username = child.child("username").getValue(String.class);

              if(location.equals(location2) && username.equals(user.getUsername())){
                child.getRef().removeValue();
              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });

        Intent next = new Intent(PaymentActivity.this, SuccessActivity.class);
        startActivity(next);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          String channelId = "binuspark";
          CharSequence channelName = "BinusPark";
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
          NotificationManager notificationManager = getSystemService(NotificationManager.class);
          notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "binuspark")
                .setSmallIcon(R.drawable.paymentqr)
                .setContentTitle("Payment Success")
                .setContentText("you sucessfully booked a parking spot.")
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, mBuilder.build());
      }
    });
  }

  public static Intent createIntent(Context context) {
    return new Intent(context, PaymentActivity.class);
  }

  public String PriceFormat(double amount)
  {
    Locale idr = new Locale("id", "ID");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(idr);

    // Set the currency to IDR
    numberFormat.setCurrency(java.util.Currency.getInstance("IDR"));

    return numberFormat.format(amount);
  }
}