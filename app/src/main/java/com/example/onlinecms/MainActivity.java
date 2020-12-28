package com.example.onlinecms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //deneme
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        progressBar = findViewById(R.id.login_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void openForgotPasswordScreen(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void openRegisterScreen(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginToSystem(View view) {
        String emailAddress = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(!(emailAddress.equals("") || password.equals(""))) {

         if(emailAddress.contains("@cmsmarmara.com")){

             progressBar.setVisibility(View.VISIBLE);

             firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         startActivity(new Intent(getApplicationContext(), AdminProfileActivity.class));
                         Toast.makeText(MainActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                         finish();
                     } else {
                         progressBar.setVisibility(View.INVISIBLE);
                         Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }
             });


         }else{

             progressBar.setVisibility(View.VISIBLE);

             firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                         Toast.makeText(MainActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                         finish();
                     } else {
                         progressBar.setVisibility(View.INVISIBLE);
                         Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
        }
        else {
            if (emailAddress.equals("")){
                emailEditText.setError("Please enter your email address!");
            }
            if (password.equals("")){
                passwordEditText.setError("Please enter your password!");
            }
        }
    }
}