package com.example.modvideoll.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.modvideoll.R;
import com.example.modvideoll.adapter.UsersAdapter;
import com.example.modvideoll.listeners.UsersListener;
import com.example.modvideoll.models.User;
import com.example.modvideoll.utilities.Constants;
import com.example.modvideoll.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UsersListener {

    private PreferenceManager preferenceManager;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TextView textErrorMessage;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(String.format(
                "%s",
                preferenceManager.getString(Constants.KEY_NAME)
        ));

        findViewById(R.id.textSignOut).setOnClickListener(v -> signOut());

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                sendFCKTokenToDatabase(task.getResult().getToken());
            }
        });
        RecyclerView userRecyclerView = findViewById(R.id.usersRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users,this);
        userRecyclerView.setAdapter(usersAdapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);

        getUsers();

    }

    private  void getUsers(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    swipeRefreshLayout.setRefreshing(false);
                    String  myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null ){
                        users.clear();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            if(myUserId.equals(documentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if(users.size() > 0){
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            textErrorMessage.setText(String.format("%s","No users available"));
                            textErrorMessage.setVisibility(View.VISIBLE);

                        }

                    }else{
                        textErrorMessage.setText(String.format("%s","No users available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCKTokenToDatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to send token" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private  void  signOut(){
        Toast.makeText(this, "Sign Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to Sign out", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void initiateVideomeeting(User user) {
        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(
                    /*context:*/this,
                    /*text:*/user.name + "" + " is not available for meeting",
                    Toast.LENGTH_SHORT
            ).show();
        }else{

            Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);

            intent.putExtra("user",user);
            intent.putExtra("type","video");

            startActivity(intent);
        }
    }

    @Override
    public void initiateAudiomeeting(User user) {
        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(
                    /*context:*/this,
                    /*text:*/user.name + "" + " is not available for meeting",
                    Toast.LENGTH_SHORT
            ).show();
        }else{
            Toast.makeText(
                    /*context:*/this,
                    /*text*/"Audio meeting with"+ user.name+"",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}