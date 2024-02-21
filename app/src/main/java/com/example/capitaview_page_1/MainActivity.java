package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView signInLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInLink = (TextView) findViewById(R.id.loginSignInLink);
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,Registration_activity.class);
                        startActivity(intent);
                        finish();
                    }
                },1000);

            }
        });
    }
}