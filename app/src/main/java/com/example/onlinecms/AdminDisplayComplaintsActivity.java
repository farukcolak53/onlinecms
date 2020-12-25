package com.example.onlinecms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

public class AdminDisplayComplaintsActivity extends AppCompatActivity {

    private TextView emailTextView;
    private TextView emailHeader;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView titleTextView;
    private TextView dateTextView;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_complaints);

        firebaseStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String address = intent.getStringExtra("address");
        String date = intent.getStringExtra("date");
        String imageUrl = intent.getStringExtra("imageUrl");
        String email = intent.getStringExtra("email");
        int status = intent.getIntExtra("status", 0);

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
        spinner.setSelection(status);
        spinner.setAdapter(adapter);



    }
}