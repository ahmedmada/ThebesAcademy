package com.eaapps.thebesacademy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.EATextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST = 1;
    static boolean isMaster = false;
    Button btn_register;
    CircleImageView selectImage;
    Spinner spinner;
    EATextInputEditText fName, fEmail, fUserId, fPhone, fMaster, fSection;
    FirebaseAuth mAuth;
    String sName, sEmail, sUserId, sPhone, sMaster, sSection;
    StringBuilder msg;
    DatabaseReference userProfile;
    StorageReference mStorage;
    List<String> items = new ArrayList<>();
    Profile profile;
    Uri resultUri;
    boolean editPhoto = false;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, Login.class));
        }
        setContentView(R.layout.activity_sign_up);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        items.add("Student");
        items.add("Admin");
        items.add("Teacher");
        mStorage = FirebaseStorage.getInstance().getReference();

        initToolbar();
        init();


    }

    private void type(String type) {
        switch (type) {
            case "Student":
                userProfile = FirebaseDatabase.getInstance().getReference().child("Student User");
                break;
            case "Admin":
                userProfile = FirebaseDatabase.getInstance().getReference().child("Admin User");
                break;
            case "Teacher":
                userProfile = FirebaseDatabase.getInstance().getReference().child("Teacher User");
                break;
        }

    }

    private void init() {
        btn_register = findViewById(R.id.btn_register);
        selectImage = findViewById(R.id.selectImage);
        fName = findViewById(R.id.nameField);
        fEmail = findViewById(R.id.emailField);
        fUserId = findViewById(R.id.userId_field);
        fPhone = findViewById(R.id.phone_field);
        fMaster = findViewById(R.id.master_field);
        fSection = findViewById(R.id.section_Field);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, items));

        fSection.setOnClickListener(this);
        fMaster.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        selectImage.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });
    }

    private void createAccount() {
        if (hasEmpty()) {
            type(items.get(spinner.getSelectedItemPosition()));
            mAuth.createUserWithEmailAndPassword(sEmail, sUserId).addOnSuccessListener(authResult -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(sUserId).build();
                authResult.getUser().updateProfile(profileUpdates);
                StorageReference filePath = mStorage.child("DonorProfile").child(resultUri.getLastPathSegment());
                filePath.putFile(resultUri).addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (!TextUtils.isEmpty(downloadUrl != null ? downloadUrl.toString() : null)) {
                        DatabaseReference container = userProfile.child(authResult.getUser().getUid());
                        profile = new Profile();
                        profile.setUrl(downloadUrl.toString());
                        profile.setId(authResult.getUser().getUid());
                        profile.setName(sName);
                        profile.setEmail(sEmail);
                        profile.setUserId(sUserId);
                        profile.setPhone(sPhone);
                        profile.setMaster(sMaster);
                        profile.setSection(sSection);
                        container.setValue(profile.addProfile());
                        if (spotsDialog.isShowing()) {
                            spotsDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(e -> {
                    if (spotsDialog.isShowing()) {
                        spotsDialog.dismiss();
                    }
                    e.printStackTrace();
                });

            }).addOnFailureListener(e -> {
                if (spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
                e.printStackTrace();
            });

        } else {

            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }

        }

    }

    private boolean hasEmpty() {
        sName = fName.getText().toString();
        sEmail = fEmail.getText().toString();
        sUserId = fUserId.getText().toString();
        sPhone = fPhone.getText().toString();
        sMaster = fMaster.getText().toString();
        sSection = fSection.getText().toString();

        if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sEmail) && !TextUtils.isEmpty(sUserId) &&
                !TextUtils.isEmpty(sPhone) && !TextUtils.isEmpty(sMaster) &&
                !TextUtils.isEmpty(sSection)) {
            return true;

        } else {
            ArrayList arr = Constants.getStringsEmpty(new String[]{sName, sEmail, sUserId, sPhone, sMaster, sSection}
                    , new String[]{"Name", "Email", "UserId", "Phone", "Master", "Section"});
            msg = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                msg.append(arr.get(i)).append(" Is Empty Field\n");
            }
            Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

        }
        return false;
    }


    private void selectList(final Context context, String[] listChoice) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_choice_type);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        dialog.show();
        dialog.setCancelable(false);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_list_choice_type, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = holder.itemView.findViewById(R.id.txt);
                textView.setText("  " + listChoice[position]);
                holder.itemView.setOnClickListener(v -> {

                    if (isMaster) {
                        sMaster = listChoice[position];
                        fMaster.setText(sMaster);

                    } else {
                        sSection = listChoice[position];
                        fSection.setText(sSection);
                    }
                    dialog.dismiss();
                });

            }

            @Override
            public int getItemCount() {
                return listChoice.length;
            }
        });
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.master_field:
                isMaster = true;
                selectList(SignUp.this, new String[]{"Computer Science", "Information Systems", "Accounting", "Business Administration"});
                break;
            case R.id.section_Field:
                isMaster = false;
                selectList(SignUp.this, new String[]{"English", "Arabic"});
                break;
            case R.id.btn_register:
                spotsDialog.show();
                createAccount();
                break;
            case R.id.selectImage:
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(SignUp.this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                selectImage.setImageURI(resultUri);
                editPhoto = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Error Activity Crop", error.getMessage());
            }
        }


    }
}
