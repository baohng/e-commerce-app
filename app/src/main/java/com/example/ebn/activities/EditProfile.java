package com.example.ebn.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ebn.R;
import com.example.ebn.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {


    private ImageView backToIcon, personalImage, coverImage;
    private Button buttonOpenGallery, buttonOpenCamera, buttonUpdateProfile;
    private EditText editTextUserName, editTextDoB, editTextGender, editTextOccupation;
    private ProgressBar progressBar;
    private static final int PICK_IMAGE_PERSONAL_REQUEST = 1;
    private static final int PICK_IMAGE_COVER_REQUEST = 2;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private StorageTask mUploadTask;
    private Uri imagePersonalUri, imageCoverUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backToIcon = findViewById(R.id.backToIcon);
        personalImage = findViewById(R.id.personalImage);
        coverImage = findViewById(R.id.imageViewCoverImage);
        buttonOpenGallery = findViewById(R.id.buttonOpenGallery);
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        progressBar = findViewById(R.id.progress_bar);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextDoB = findViewById(R.id.editTextDoB);
        editTextGender = findViewById(R.id.editTextGender);
        editTextOccupation = findViewById(R.id.editTextOccupation);

        storageRef = FirebaseStorage.getInstance().getReference("profileUploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        buttonUpdateProfile.setOnClickListener(this);
        backToIcon.setOnClickListener(this);
        personalImage.setOnClickListener(this);
        buttonOpenGallery.setOnClickListener(this);
        buttonOpenCamera.setOnClickListener(this);

        retrieveProfile();
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
            case R.id.buttonUpdateProfile:
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(EditProfile.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile();
                    uploadFile();
                }
                break;
        }
    }
    private void retrieveProfile() {
        databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            String fullName, phoneNum, descript,gender, occupation, dob, personalImageUrl, coverImageUrl;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    fullName = user.fullName;
                    phoneNum = user.phoneNumber;
                    descript = user.descript;
                    gender = user.gender;
                    occupation = user.occupation;
                    dob = user.dob;
                    personalImageUrl = user.mPersonalImageUrl;
                    coverImageUrl = user.mCoverImageUrl;

                    editTextUserName.setText(fullName);
                    editTextDoB.setText(dob);
                    editTextGender.setText(gender);
                    editTextOccupation.setText(occupation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void updateProfile() {
        retrieveProfile();
    }

    private void uploadFile() {

        //upload personal image (avatar)
        if (imagePersonalUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imagePersonalUri));
            fileReference.putFile(imagePersonalUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(EditProfile.this, "Upload successful", Toast.LENGTH_LONG).show();
                            databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("mPersonalImageUrl")
                                    .setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No personal image file selected",Toast.LENGTH_LONG).show();
        }

        //Upload cover image
        if (imageCoverUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageCoverUri));
            fileReference.putFile(imageCoverUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(EditProfile.this, "Upload successful", Toast.LENGTH_LONG).show();
                            databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("mCoverImageUrl")
                                    .setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No personal image file selected",Toast.LENGTH_LONG).show();
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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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