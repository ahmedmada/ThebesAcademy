package com.eaapps.thebesacademy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.eaapps.thebesacademy.Files.ReadFilesDoctors;
import com.eaapps.thebesacademy.Files.UploadFile;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.Model.RetrieveData;
import com.eaapps.thebesacademy.PDF.PdfListActivity;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.EATextInputEditText;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.eaapps.thebesacademy.Utils.ThebesInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button sigIn, signUp;
    EATextInputEditText userField, passwordField;
    FirebaseAuth mAuth;
    RetrieveData<Profile> retrieveData;
    DatabaseReference databaseReference;
    Spinner spinner;
    List<String> items = new ArrayList<>();
    String sUserId, sPassword;
    StringBuilder msg;
    StoreKey storeKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeKey = new StoreKey(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Constants.homeClasses(storeKey.getUser())));
        }
        setContentView(R.layout.activity_login);
        items.add("Student");
        items.add("Admin");
        items.add("Teacher");
        init();
        retrieveData = new RetrieveData<Profile>(Login.this) {
        };

//        startActivity(new Intent(Login.this, PdfListActivity.class));
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, items));
        sigIn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.btn_signUp);
        userField = findViewById(R.id.userId_field);
        passwordField = findViewById(R.id.password_field);
        sigIn.setOnClickListener(this);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                type(items.get(spinner.getSelectedItemPosition()));
                if (hasEmpty()) {
                    retrieveData.hasUser(Profile.class, databaseReference, new ThebesInterface.CallBackFindUser<Profile>() {
                                @Override
                                public void hasUser(Profile object) {
                                    if (object != null) {
                                        if (object.getUserId().equals(sUserId)) {
                                            mAuth.signInWithEmailAndPassword(object.getEmail(), sUserId).addOnSuccessListener(authResult ->
                                            {
//                                                storeKey.setUser(items.get(spinner.getSelectedItemPosition()));
//
//                                                startActivity(new Intent(Login.this,
//                                                        Constants.homeClasses(items.get(spinner.getSelectedItemPosition()))));

//                                                startActivity(new Intent(Login.this, Table.class));
                                                startActivity(new Intent(Login.this, ReadFilesDoctors.class));

                                            }).addOnFailureListener(e -> e.printStackTrace());

                                        }
                                    }
                                }

                                @Override
                                public void hasUser(boolean f) {
                                    if (!f) {
                                        Constants.customDialogAlter(Login.this, R.layout.custom_dialog2,
                                                "This UserId Is Not Registered In The App Before.");
                                    }

                                }
                            }

                    );
                }
                break;
            case R.id.btn_signUp:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;
        }
    }

    private void type(String type) {
        switch (type) {
            case "Student":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Student User");
                break;
            case "Admin":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin User");
                break;
            case "Teacher":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher User");
                break;
        }

    }

    private boolean hasEmpty() {
        sUserId = userField.getText().toString();
        sPassword = passwordField.getText().toString();
        if (!TextUtils.isEmpty(sUserId) && !TextUtils.isEmpty(sPassword)) {
            return true;

        } else {
            ArrayList arr = Constants.getStringsEmpty(new String[]{sUserId, sPassword}
                    , new String[]{"UserId", "Password"});
            msg = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                msg.append(arr.get(i)).append(" Is Empty Field\n");
            }
            Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

        }
        return false;
    }

}
