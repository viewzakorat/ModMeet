package com.example.modvideoll.listeners;

import com.example.modvideoll.models.User;

public interface UsersListener {

    void initiateVideomeeting(User user);

    void initiateAudiomeeting(User user);
}
