package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessActivity extends AppCompatActivity {

  Button btn1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_success);

    btn1 = findViewById(R.id.homescreenBtn);

    btn1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent back = new Intent(SuccessActivity.this, DashboardActivity.class);
        startActivity(back);
      }
    });
  }
}