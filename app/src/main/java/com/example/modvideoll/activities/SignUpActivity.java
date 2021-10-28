package com.example.modvideoll.activities;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.modvideoll.R;
import com.example.modvideoll.utilities.Constants;
import com.example.modvideoll.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    private MaterialButton buttonSignUp;
    private ProgressBar signUpProgressBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        preferenceManager = new PreferenceManager(getApplicationContext());
        findViewById(R.id.ButtonSignIn).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SignInActivity.class)));
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        buttonSignUp.setOnClickListener(v -> {
            if (inputName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else if (inputEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                Toast.makeText(SignUpActivity.this, "Please Entry valid email", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please confirm your password ", Toast.LENGTH_SHORT).show();
            } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Password and Confirm password must be same", Toast.LENGTH_SHORT).show();
            } else {
                signUp();

            }
        });

    }
    private void signUp(){

        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, inputName.getText().toString()); //ของเรามีแค่ชื่อ ไม่ม่ Firstname Lastname
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());

        user.put(Constants.KEY_PERMISSION,  "PATIENT");

        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);

                        preferenceManager.putString(Constants.KEY_NAME, inputName.getText().toString());
                        preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString());

                        preferenceManager.putString(Constants.KEY_PERMISSION,  "PATIENT" );

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }


                });


        FirebaseFirestore database1 = FirebaseFirestore.getInstance();
        database1.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}






