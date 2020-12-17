package com.example.onlinecms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView textView;
    private EditText emailEditText;
    private EditText nameSurnameEditText;
    private EditText passwordEditText;
    private EditText newPasswordEditText;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        textView = findViewById(R.id.profile_settings_email);
        emailEditText = findViewById(R.id.profile_settings_email_edit_text);
        nameSurnameEditText = findViewById(R.id.profile_settings_name_surname_edit_text);

        passwordEditText = findViewById(R.id.profile_settings_password_edit_text);
        newPasswordEditText = findViewById(R.id.profile_settings_new_password_edit_text);

        progressBar = findViewById(R.id.profile_settings_progress_bar);

        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        emailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        nameSurnameEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void profileSettingsSaveChanges(View view) {
        String password = passwordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String nameSurname = nameSurnameEditText.getText().toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        if (!password.equals("")) {
            progressBar.setVisibility(View.VISIBLE);

            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nameSurname).build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Display name: ", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                    System.out.println("Name changed succesfully!");
                                }
                            }
                        });

                        if (!newPassword.equals("") && newPassword.length() >= 6) {

                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileSettingsActivity.this, "Password changed correctly!", Toast.LENGTH_SHORT).show();
                                        ProfileSettingsActivity.super.onBackPressed();
                                    }
                                    else {
                                        Toast.makeText(ProfileSettingsActivity.this, "Ups..!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Toast.makeText(ProfileSettingsActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}