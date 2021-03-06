package com.example.modvideoll.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.modvideoll.activities.IncomingInvitationActivity;
import com.example.modvideoll.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM","Token: "+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);  /*clip9*/

        if(type != null){
            if(type.equals(Constants.REMOTE_MSG_INVITATION)){
                Intent intent = new Intent(getApplicationContext(), IncomingInvitationActivity.class); /*clip9ทั้งหมดถึงล่างเลย*/
                intent.putExtra(
                        Constants.KEY_NAME,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE)
                );
                intent.putExtra(
                        Constants.KEY_NAME,
                        remoteMessage.getData().get(Constants.KEY_NAME)
                );
                intent.putExtra(
                        Constants.KEY_EMAIL,
                        remoteMessage.getData().get(Constants.KEY_EMAIL)
                );
                intent.putExtra(
                        Constants.REMOTE_MSG_INVITER_TOKEN,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN)
                );
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_ROOM,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM)
                );

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent); /*clip9ถึงนี่!*/

            }else if(type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)){
                Intent intent = new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(
                        Constants.REMOTE_MSG_INVITATION_RESPONSE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE)
                );
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

        }


    }
}
