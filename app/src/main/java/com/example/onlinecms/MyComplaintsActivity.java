package com.example.onlinecms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.onlinecms.model.Complaint;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MyComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ComplaintAdapter complaintAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);

        recyclerView = findViewById(R.id.my_complaints_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String path = firebaseAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<Complaint> options =
                new FirebaseRecyclerOptions.Builder<Complaint>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(path), Complaint.class)
                        .build();

        complaintAdapter = new ComplaintAdapter(options);
        recyclerView.setAdapter(complaintAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        complaintAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        complaintAdapter.stopListening();
    }
}