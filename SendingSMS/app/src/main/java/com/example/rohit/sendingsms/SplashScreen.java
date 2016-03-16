package com.example.rohit.sendingsms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        final String strSMSNumber = sharedPreferences.getString("sms_number", "");
        final String strCallNumber = sharedPreferences.getString("call_number", "");


        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent intent = null;
              if (strSMSNumber.length() > 0  && strCallNumber.length() > 0){

                  intent = new Intent(SplashScreen.this,MainActivity.class);
                  intent.putExtra("sms_number", strSMSNumber);
                  intent.putExtra("call_number",strCallNumber);
              } else {
                  intent = new Intent(SplashScreen.this,LoginActivity.class);
              }

              finish();
              startActivity(intent);
          }
      }, 2000);
    }



}
