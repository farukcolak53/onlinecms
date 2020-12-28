package com.example.onlinecms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class AdminDisplayComplaintsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView emailTextView;
    private TextView emailHeader;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView titleTextView;
    private TextView dateTextView;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference mRef;
    private Spinner spinner;
    private String email;
    private int count;
    private int statu;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_complaints);

        firebaseStorage = FirebaseStorage.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String address = intent.getStringExtra("address");
        String date = intent.getStringExtra("date");
        String imageUrl = intent.getStringExtra("imageUrl");
        email = intent.getStringExtra("email");
        count = intent.getIntExtra("count",0);
        userID = intent.getStringExtra("id");
        statu = intent.getIntExtra("status", 0);

        emailHeader = findViewById(R.id.admin_each_complaint_user_email);
        emailTextView = findViewById(R.id.admin_each_complaint_email_content);
        titleTextView = findViewById(R.id.admin_each_complaint_title_content);
        addressTextView = findViewById(R.id.admin_each_complaint_address_content);
        descriptionTextView = findViewById(R.id.admin_each_complaint_description_content);
        dateTextView = findViewById(R.id.admin_each_complaint_date_content);
        imageView = findViewById(R.id.admin_each_complaint_image_view);
        spinner = (Spinner) findViewById(R.id.admin_each_complaint_spinner);

        emailHeader.setText(email);
        emailTextView.setText(email);
        titleTextView.setText(title);
        addressTextView.setText(address);
        descriptionTextView.setText(description);
        dateTextView.setText(date);
        imageView.setVisibility(View.GONE);

        if (!imageUrl.equals("")) {
            GlideApp.with(getApplicationContext())
                    .load(firebaseStorage.getReferenceFromUrl(imageUrl))
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(statu);
            }
        });
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String status = (String) parent.getItemAtPosition(position);
        if("Closed".equals(status) && statu != 1){
            mRef.child(userID).child(Integer.toString(count)).child("status").setValue(1);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(AdminDisplayComplaintsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else if ("Open".equals(status) && statu != 0){
            mRef.child(userID).child(Integer.toString(count)).child("status").setValue(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}