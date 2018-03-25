package com.eaapps.thebesacademy.Files;

import android.database.Cursor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eaapps.thebesacademy.PDF.PdfListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.eaapps.thebesacademy.R;

public class UploadFile extends AppCompatActivity {

    // Folder path for Firebase Storage.
    String Storage_Path = "Subjects/";

    // Root Database Name for Firebase Database.
    String files = "Files";

    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText SubjectName ;
    EditText ChapterName ;

    // Creating ImageView.
//    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    private String mCurrentUserId;
    private FirebaseAuth mAuth;

    // Image request code for onActivityResult() .
    int FILE_Request_Code = 7;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(files);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        //Assign ID'S to button.
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);

        // Assign ID's to EditText.
        SubjectName = (EditText)findViewById(R.id.SubjectNameEditText);
        ChapterName = (EditText)findViewById(R.id.ChapterNameEditText);

        // Assign ID'S to image view.
//        SelectImage = (ImageView)findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(UploadFile.this);

        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UploadFile.this, PdfListActivity.class);
                startActivityForResult(i, FILE_Request_Code);

                // Creating intent.
//                Intent intent = new Intent();
//
//                // Setting intent type as image to select image from phone storage.
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), FILE_Request_Code);


//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/pdf");
//                startActivityForResult(Intent.createChooser(intent, "Please Select PDF File"), Image_Request_Code);

            }
        });


        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 1212:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    FilePathUri = data.getData();
//                    String uriString = FilePathUri.toString();
//                    File myFile = new File(uriString);
//                    String path = myFile.getAbsolutePath();
//                    String displayName = null;
//
//                    if (uriString.startsWith("content://")) {
//                        Cursor cursor = null;
//                        try {
//                            cursor = this.getContentResolver().query(FilePathUri, null, null, null, null);
//                            if (cursor != null && cursor.moveToFirst()) {
//                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                            }
//                            ChooseButton.setText("File Selected");
//                        } finally {
//                            cursor.close();
//                        }
//                    } else if (uriString.startsWith("file://")) {
//                        displayName = myFile.getName();
//                    }
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.v("bbbbbbbbbbbbb","bbbb");
//        Log.v("bbbbbbbbbbbbb", String.valueOf(data.getData()));
        Log.v("bbbbbbbbbbbbb",data.getStringExtra("filepath"));

        if (requestCode == FILE_Request_Code && resultCode == RESULT_OK &&
                data != null && data.getStringExtra("filepath") != null) {

            Log.v("bbbbbbbbbbbbb","test");
            Log.v("bbbbbbbbbbbbb",data.getStringExtra("filepath"));

//            FilePathUri = data.getData();
            FilePathUri = Uri.parse(data.getStringExtra("filepath"));
            try {

                // Getting selected image into Bitmap.
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
//                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("File Selected");

            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("File is Uploading...");

            // Showing progressDialog.
            progressDialog.show();


            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
//            InputStream stream = null;
//            try {
//                stream = new FileInputStream(new File(String.valueOf(FilePathUri)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            Uri file = Uri.fromFile(new File(String.valueOf(FilePathUri)));

            // Adding addOnSuccessListener to second StorageReference.
//            if (stream != null) {
                storageReference2nd.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                // Getting image name from EditText and store into string variable.
                                String chapter = ChapterName.getText().toString().trim();
                                String subject = SubjectName.getText().toString().trim();

                                // Hiding the progressDialog after done uploading.
                                progressDialog.dismiss();

                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "File Uploaded Successfully ", Toast.LENGTH_LONG).show();


                                String file_data =  mCurrentUserId + "/" + subject + "/" + chapter;


    //                            @SuppressWarnings("VisibleForTests")
    //                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName,
    //                                    taskSnapshot.getDownloadUrl().toString());

                                // Getting image upload ID.
    //                            String ImageUploadId = databaseReference.push().getKey();

                                // Adding image upload id s child element into databaseReference.
    //                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                                databaseReference.child(file_data).setValue(downloadUrl.toString(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if(databaseError != null){

                                            Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                        }

                                    }
                                });

                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                // Hiding the progressDialog.
                                progressDialog.dismiss();

                                // Showing exception erro message.
                                Toast.makeText(UploadFile.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })

                        // On progress change upload time.
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                // Setting progressDialog Title.
                                progressDialog.setTitle("File is Uploading...");

                            }
                        });
            }
//        }
        else {

            Toast.makeText(UploadFile.this, "Please Select File or Add Details", Toast.LENGTH_LONG).show();

        }
    }


}

