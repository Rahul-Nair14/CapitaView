package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    ImageView splash;
    Drawable cVIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initializing everything
        //Use Glide to load GIF format
        cVIcon =  getDrawable(R.drawable.splashimage);
        splash = (ImageView)findViewById(R.id.SplashImage);
        Glide.with(this).load(cVIcon).into(splash);

        //Adding Loginprefs to Cache, this is reset whenever the app is installed
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Redirect user based on login state
                // Uncomment this when you release the final beta
                if (isLoggedIn) {
                    // User is logged in, redirect to the appropriate activity
                    Intent intent = new Intent(SplashActivity.this, MainDashboard.class);
                    startActivity(intent);
                    finish(); // Finish splash activity to prevent user from coming back here by pressing back button
                } else {
                    // User is not logged in, redirect to the login activity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish splash activity to prevent user from coming back here by pressing back button
                }
            }
        },4500);

        scheduleCacheClearing(getApplicationContext());
    }

    //Uses Alarm manager to set up the scheduling of the cache clearance
    public void scheduleCacheClearing(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CacheClearing.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at 00:00
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        // Repeat the alarm daily
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}