package com.example.modvideoll.utilities;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_PERMISSION = "permission";


    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";


    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization"; /*clip9*/
    public static final String REMOTE_MSG_CONTENT_TYPE ="Content-Type"; /*clip9*/

    public static final String REMOTE_MSG_TYPE = "type"; /*clip9*/
    public static final String REMOTE_MSG_INVITATION = "invitation"; /*clip9*/
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType"; /*clip9*/
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken"; /*clip9*/
    public static final String REMOTE_MSG_DATA = "data"; /*clip9*/
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids"; /*clip9*/

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public  static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";

    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static  final  String REMOTE_MSG_MEETING_ROOM = "meetingRoom";
    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();     /*clip9*/
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAO6BaXTQ:APA91bES5-GhfCJg8aJ9-7S4Iwt8iN8W9pje5ShKvs-WchJX_CbCG-8Suv4SjkkxjS5EDZQ8CvWOTzHmIbg--D8zLRA98maG5eXxk8cgCkVpZzZ1mQ9wO2MXyRI-RqmeWeWg452Fc-wu"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return  headers;
    }
}
