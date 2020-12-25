package com.example.onlinecms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminProfileActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_admin_profile);
        TextView textView = findViewById(R.id.admin_profile_email);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    public void signOutFromSystemAdmin(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(
                AdminProfileActivity.this);
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

    // admin ekleme
    public void createNewAdmin(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterAdminActivity.class);
        startActivity(intent);
    }

    // butu sikayetleri gosterme sayfsina yonlendirme
    public void displayAllComplaints(View view) {
        Intent intent = new Intent(this, AllActivities.class);
        startActivity(intent);
    }

    //
    public void openAdminProfileSettings(View view) {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }
}
