package com.example.ebn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ebn.R;
import com.example.ebn.databinding.ActivityMainBinding;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {


    private ImageView backToIcon, personalImage, coverImage;
    private Button buttonOpenGallery, buttonOpenCamera;
    private static final int PICK_IMAGE_PERSONAL_REQUEST = 1;
    private static final int PICK_IMAGE_COVER_REQUEST = 2;
    Uri imagePersonalUri, imageCoverUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backToIcon = findViewById(R.id.backToIcon);
        personalImage = findViewById(R.id.personalImage);
        coverImage = findViewById(R.id.imageViewCoverImage);
        buttonOpenGallery = findViewById(R.id.buttonOpenGallery);
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera);


        backToIcon.setOnClickListener(this);
        personalImage.setOnClickListener(this);
        buttonOpenGallery.setOnClickListener(this);
        buttonOpenCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToIcon:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
            case R.id.personalImage:
                selectPersonalImage();
                break;
            case R.id.buttonOpenGallery:
                selectCoverImage();
                break;
            case R.id.buttonOpenCamera:
                openCamera();
                break;
        }
    }

    private void openCamera() {
    }

    private void selectPersonalImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_PERSONAL_REQUEST);
    }

    private void selectCoverImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_COVER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_PERSONAL_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imagePersonalUri = data.getData();
            personalImage.setImageURI(imagePersonalUri);
        }

        if (requestCode == PICK_IMAGE_COVER_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageCoverUri = data.getData();
            coverImage.setImageURI(imageCoverUri);
        }
    }
}