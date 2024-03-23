package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class MainActivity extends AppCompatActivity {

    TextView signInLink;
    EditText loginUserNameVar, loginPasswordVar;
    Button loginButtonVar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing
        signInLink = (TextView) findViewById(R.id.loginSignInLink);
        loginUserNameVar = (EditText)findViewById(R.id.LoginPageUserNameEntry);
        loginPasswordVar = (EditText)findViewById(R.id.LoginPagePassWordEntry);
        loginButtonVar = (Button)findViewById(R.id.LoginButton);

        firebaseAuth = FirebaseAuth.getInstance();

        //Registration activity text onClick
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,Registration_activity.class);
                        startActivity(intent);
                    }
                },1000);

            }
        });

        loginButtonVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = loginUserNameVar.getText().toString();
                String passWord = loginPasswordVar.getText().toString();

                if(isValidEmail(userName) && !(userName.isEmpty()) && !(passWord.isEmpty())) {

                    firebaseAuth.signInWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Welcome "+ userName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,MainDashboard.class);
                            //Sending username for displaying in all other activities
                            intent.putExtra("userNameForUse",userName);
                            intent.putExtra("activityName","Login");
                            startActivity(intent);
                            //Telling app that we have logged in
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            //Deleting Text to refresh
                            loginPasswordVar.setText("");
                            loginUserNameVar.setText("");
                            finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                                handleException(e);
                        }
                    });
                }

                else {
                    Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Checks if email is of valid format
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Handles all exceptions
    private void handleException(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(MainActivity.this, "Invalid user. Please sign up.", Toast.LENGTH_SHORT).show();
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(MainActivity.this, "Invalid credentials or Invalid user, Please try again or sign up", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Login failed. Please try again later.", Toast.LENGTH_SHORT).show();
            Log.e("LoginError", "Error: " + e.getMessage());
        }
    }

}