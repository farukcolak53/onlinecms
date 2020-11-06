package com.example.onlinecms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {


    static int PReqCode = 1;
    static int REQUESCODE = 1;

    private EditText userEmail, userPassword, userPAssword2;
    private EditText userName;
    //    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ini views
        userEmail = findViewById(R.id.register_email_edit_text);
        userPassword = findViewById(R.id.register_password_edit_text);
        userPAssword2 = findViewById(R.id.register_password_again_edit_text);
         userName = findViewById(R.id.register_name_edit_text);
//        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.register_button_sign_up);
//        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
//                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPAssword2.getText().toString();
                final String name = userName.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)) {


                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
//                    loadingProgress.setVisibility(View.INVISIBLE);


                } else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid

                    createUserAccount(email, name, password);
                }


            }
        });




    }

    private void createUserAccount(String email, final String name, String password) {


        // this method create user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                            showMessage("Account created");
                            UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            mAuth.getCurrentUser().updateProfile(profleUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                // user info updated successfully
                                                showMessage("Register Complete");
                                                updateUI();
                                            }

                                        }
                                    });

                        } else {

                            // account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
//                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });


    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeActivity);
        finish();

    }

    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }


}