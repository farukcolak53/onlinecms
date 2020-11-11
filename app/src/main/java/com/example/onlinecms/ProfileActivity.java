package com.example.onlinecms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = findViewById(R.id.profile_email);

        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    public void signOutFromSystem(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(
                ProfileActivity.this);
        alert.setTitle("Warning!");
        alert.setMessage("Are you sure you want to sign out?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    public void createNewComplaint(View view) {
        Intent intent = new Intent(this, NewComplaintActivity.class);
        startActivity(intent);
    }

    public void displayMyComplaints(View view) {
        Intent intent = new Intent(this, MyComplaintsActivity.class);
        startActivity(intent);
    }

    public void openProfileSettings(View view) {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }
}