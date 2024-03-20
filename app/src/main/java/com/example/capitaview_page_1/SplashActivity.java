package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView splash;
    Drawable cVIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cVIcon =  getDrawable(R.drawable.splashimage);
        splash = (ImageView)findViewById(R.id.SplashImage);
        Glide.with(this).load(cVIcon).into(splash);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Redirect user based on login state
                // Uncomment this when you release the final beta
//                if (isLoggedIn) {
//                    // User is logged in, redirect to the appropriate activity
//                    Intent intent = new Intent(SplashActivity.this, MainDashboard.class);
//                    startActivity(intent);
//                    finish(); // Finish splash activity to prevent user from coming back here by pressing back button
//                } else {
                    // User is not logged in, redirect to the login activity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish splash activity to prevent user from coming back here by pressing back button
              //  }
            }
        },4500);


    }
}