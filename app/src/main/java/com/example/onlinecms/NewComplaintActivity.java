package com.example.onlinecms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.onlinecms.model.Complaint;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.TimeZone;

public class NewComplaintActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int PICK_IMAGE = 200;
    private static final int TAKE_PHOTO = 100;
    private static final int LOCATION = 101;
    private EditText titleText;
    private EditText descriptionText;
    private TextView addressText;
    private DatabaseReference mRef;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        imageView = findViewById(R.id.new_complaint_image_view);
        titleText = findViewById(R.id.new_complaint_title_edit_text);
        descriptionText = findViewById(R.id.new_complaint_description_edit_text);
        addressText = findViewById(R.id.new_complaint_location_text_view);

        imageUri = Uri.EMPTY;

        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference();

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, TAKE_PHOTO);
        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        NewComplaintActivity.this);
                alert.setTitle("Warning!");
                alert.setMessage("Are you sure you want to delete this photo?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageView.setVisibility(View.GONE);
                        dialogInterface.dismiss();
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
    }

    public void takePhoto(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void getLoc(View view){
        Intent intent = new Intent(this, LocationHelper.class);
        startActivityForResult(intent, LOCATION);
    }

    public void saveComplaint(View view){
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String address = addressText.getText().toString();
        String id = user.getUid();
        HashMap<String, Object> complaint = new HashMap<>();
        if(title.equals("") || description.equals("")){
            if (title.equals(""))
                titleText.setError("Provide a title");
            if (description.equals(""))
                descriptionText.setError("Provide a description");
        }
        else{
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

            mRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int count =(int) snapshot.getChildrenCount();
                        if(!Uri.EMPTY.equals(imageUri))
                            uploadImageToStorage(id, count, title, address, description);
                        else {
                            upload(id,title,description,address, "", count);
                            finish();
                        }
                    }
                    else{
                        if(!Uri.EMPTY.equals(imageUri))
                            uploadImageToStorage(id, 0, title, address, description);
                        else {
                            upload(id,title,description,address, "", 0);
                            finish();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void uploadImageToStorage(String id, int count, String title, String address, String description) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        String path = "images/" + id + "*" + count;
        StorageReference reference = mStorageRef.child(path);

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewComplaintActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                String imageUrl = "https://firebasestorage.googleapis.com/b/cmsmarmara.appspot.com/o/images/" + id + "*" + count;
                upload(id,title,description,address, imageUrl, count);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        finish();
                    }
                }, 1000);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                //progressDialog.setMessage("Uploaded " + (int)progress + "%");
                progressDialog.setMessage("Uploading");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PHOTO){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
        }

        if(requestCode == LOCATION){
            if(resultCode == RESULT_OK){
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                Location location = (Location) data.getExtras().get("location");
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                TextView locationText = findViewById(R.id.new_complaint_location_text_view);
                locationText.setVisibility(View.VISIBLE);
                locationText.setText(address);
            }
        }
    }

    public void addFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void upload(String id, String title, String desc, String address, String url, int count){

        String date = Calendar.getInstance(TimeZone.getTimeZone("GMT+3")).getTime().toString();

        Complaint complaint = new Complaint(title,desc,address, url, date);
        mRef.child(id).child(Integer.toString(count)).updateChildren(complaint.toMap());
    }
}