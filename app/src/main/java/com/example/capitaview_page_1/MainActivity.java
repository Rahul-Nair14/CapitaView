package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView signInLink;
    EditText loginUserNameVar, loginPasswordVar;
    Button loginButtonVar;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInLink = (TextView) findViewById(R.id.loginSignInLink);
        loginUserNameVar = (EditText)findViewById(R.id.LoginPageUserNameEntry);
        loginPasswordVar = (EditText)findViewById(R.id.LoginPagePassWordEntry);
        loginButtonVar = (Button)findViewById(R.id.LoginButton);

        firebaseAuth = FirebaseAuth.getInstance();

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
                            startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            }
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

}