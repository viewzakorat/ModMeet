package com.example.modvideoll.listeners;

import android.widget.Toast;

import com.example.modvideoll.activities.SignUpActivity;
import com.example.modvideoll.models.User;

public interface UsersListener {

    void initiateVideomeeting(User user);

    void initiateAudiomeeting(User user);

}
