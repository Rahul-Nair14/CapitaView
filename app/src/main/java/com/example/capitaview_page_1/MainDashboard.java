package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private static String dashBoardUserNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        dashboardLogoutVar = (Button) findViewById(R.id.LogoutButton);
        dashboardManageVar = (ImageButton)findViewById(R.id.dashboardManageButton);
        dashboardViewVar = (ImageButton)findViewById(R.id.dashboardViewButton);
        dashboardChartsVar = (ImageButton)findViewById(R.id.dashboardChartsButton);
        dashboardNewsVar = (ImageButton)findViewById(R.id.dashboardNewsButton);
        dashBoardUserName = (TextView)findViewById(R.id.dashboardUserName);


        Intent intent = getIntent();
        if(Objects.equals(intent.getStringExtra("activityName"), "Login")) {
            String temp = intent.getStringExtra("userNameForUse");
            dashBoardUserName.setText(temp);
            dashBoardUserNameString = temp;
        }
        else
            dashBoardUserName.setText("Welcome " + dashBoardUserNameString);

        dashboardLogoutVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                Toast.makeText(MainDashboard.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainDashboard.this, MainActivity.class));
                finish();
            }
        });

        dashboardNewsVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, NewsActivity.class));
            }
        });

        dashboardManageVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String userNameFromLogin = intent.getStringExtra("userNameForUse");

                Intent manageIntent = new Intent(MainDashboard.this,ManageActivity.class);
                manageIntent.putExtra("userNameFromLogin",userNameFromLogin);
                manageIntent.putExtra("activityName","mainDashBoard");
                startActivity(manageIntent);

            }
        });

        dashboardChartsVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, ChartsActivity.class));
            }
        });

        dashboardViewVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this, ViewActivity.class));
            }
        });
    }

}