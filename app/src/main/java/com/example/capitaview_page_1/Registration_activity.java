package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Registration_activity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    EditText registerUserNameVar, registerPasswordVar, registerCPasswordVar, registerFullNameVar;
    Button registerButtonVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        registerFullNameVar = (EditText)findViewById(R.id.registerPageFullNameEntry);
        registerCPasswordVar = (EditText)findViewById(R.id.registerPageCPasswordEntry);
        registerPasswordVar = (EditText)findViewById(R.id.registerPagePassWordEntry);
        registerUserNameVar = (EditText)findViewById(R.id.registerPageUserNameEntry);

        registerButtonVar = (Button) findViewById(R.id.RegisterButton);
        registerButtonVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = registerUserNameVar.getText().toString();
                String passWord = registerPasswordVar.getText().toString();
                String cpassword = registerCPasswordVar.getText().toString();
                String fullName = registerFullNameVar.getText().toString();

                if(userName.isEmpty() || passWord.isEmpty() || cpassword.isEmpty() || fullName.isEmpty())
                    Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_SHORT).show();

                else if(!passWord.equals(cpassword))
                    Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT).show();

                else if(passWord.length() < 6)
                    Toast.makeText(getApplicationContext(),"Password should have at least 6 characters",Toast.LENGTH_SHORT).show();

                else if(MainActivity.isValidEmail(userName)) {
                    firebaseAuth.createUserWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registration_activity.this,MainActivity.class);
                                startActivity(intent);
                                registerPasswordVar.setText("");
                                registerUserNameVar.setText("");
                                registerFullNameVar.setText("");
                                registerCPasswordVar.setText("");
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                // Email already exists
                                Toast.makeText(Registration_activity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other errors
                                Log.e("SignUpActivity", "Error: " + e.getMessage());
                                Toast.makeText(Registration_activity.this, "Sign up failed. Please try again", Toast.LENGTH_SHORT).show();
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
}