package com.example.onlinecms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class NewComplaintActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int PICK_IMAGE = 200;
    private static final int TAKE_PHOTO = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        imageView = findViewById(R.id.new_complaint_image_view);

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
                        //imageView.setImageDrawable(null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PHOTO){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
            imageView.setVisibility(View.VISIBLE);
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            //Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(captureImage);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void addFromGallery(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(intent, RESULT_LOAD_IMAGE);
        startActivityForResult(intent, PICK_IMAGE);
    }
}