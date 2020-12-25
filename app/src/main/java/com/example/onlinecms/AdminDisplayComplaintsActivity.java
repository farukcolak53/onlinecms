package com.example.onlinecms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

public class AdminDisplayComplaintsActivity extends AppCompatActivity {

    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView titleTextView;
    private TextView dateTextView;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;


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

        titleTextView = findViewById(R.id.admin_each_complaint_title_content);
        addressTextView = findViewById(R.id.admin_each_complaint_address_content);
        descriptionTextView = findViewById(R.id.admin_each_complaint_description_content);
        dateTextView = findViewById(R.id.admin_each_complaint_date_content);
        imageView = findViewById(R.id.admin_each_complaint_image_view);

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

    }
}