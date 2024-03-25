package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainDashboard extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button dashboardLogoutVar;
    ImageButton dashboardNewsVar,dashboardManageVar,dashboardViewVar,dashboardChartsVar;
    TextView dashBoardUserName;
    public static String dashBoardUserNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        //Initializing everything and getting Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        dashboardLogoutVar = (Button) findViewById(R.id.LogoutButton);
        dashboardManageVar = (ImageButton)findViewById(R.id.dashboardManageButton);
        dashboardViewVar = (ImageButton)findViewById(R.id.dashboardViewButton);
        dashboardNewsVar = (ImageButton)findViewById(R.id.dashboardNewsButton);
        dashBoardUserName = (TextView)findViewById(R.id.dashboardUserName);

        //Getting the LoginPrefs so that we can store the username in the cache
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = getIntent();

        //If the user has come from login activity, it will set up the name correctly from login
        if(Objects.equals(intent.getStringExtra("activityName"), "Login")) {
            String temp = intent.getStringExtra("userNameForUse");
            dashBoardUserName.setText(temp);
            dashBoardUserNameString = temp;
            //Stores the username in cache for later usage if Login activity is not triggered
            editor.putString("UsernameKey", dashBoardUserNameString);
            editor.apply();

        }
        //Getting the username from cache in case user switches apps and skips Login
        else if(sharedPreferences.getBoolean("isLoggedIn",true)) {
            dashBoardUserNameString = sharedPreferences.getString("UsernameKey", "");
            dashBoardUserName.setText(dashBoardUserNameString);
        }
        //Edge case
        else
            dashBoardUserName.setText("Welcome " + dashBoardUserNameString);

        //Logging out the user from Firebase and the application
        dashboardLogoutVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();

                //Logs out the user so we update cache as well
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Toast.makeText(MainDashboard.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainDashboard.this, MainActivity.class));
                finish();
            }
        });

        //News activity start
        dashboardNewsVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, NewsActivity.class));
            }
        });

        //Manage activity start
        dashboardManageVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainDashboard.this,ManageActivity.class));
            }
        });

        //View activity start
        dashboardViewVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, ViewActivity.class));
            }
        });
    }

}