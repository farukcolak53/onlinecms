package com.example.onlinecms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.onlinecms.model.Complaint;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class AllActivities extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    RecyclerView recyclerView;
    AllComplaintAdapter adapter;
    private ArrayList<String> ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_activities);
        mRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference();
        recyclerView = findViewById(R.id.all_complaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ids = new ArrayList<>();
        adapter = new AllComplaintAdapter(this);

        test();

        //adapter.addAll(complaints);


//        int length = ids.size();
//        for (int i = 0; i < length; i++) {
//            FirebaseRecyclerOptions<Complaint> options =
//                    new FirebaseRecyclerOptions.Builder<Complaint>()
//                            .setQuery(FirebaseDatabase.getInstance().getReference().child(ids.get(i)), Complaint.class)
//                            .build();
//
//            complaintAdapter = new ComplaintAdapter(options);
//            recyclerView.setAdapter(complaintAdapter);
//        }


    }

    public void test(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Complaint> complaints = new ArrayList<>();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()
                    ) {
                        String a = ds.getKey();
                        ids.add(a);
                        for (DataSnapshot childDs: ds.getChildren()
                        ) {
                            Complaint complaint = childDs.getValue(Complaint.class);
                            complaints.add(complaint);
                            //adapter.addSingleComplaint(complaint);
                        }
                    }
                    adapter.addAll(complaints);
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUI(){
        recyclerView.setAdapter(adapter);
    }

}