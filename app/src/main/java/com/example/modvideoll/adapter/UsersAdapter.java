package com.example.modvideoll.adapter;

import static com.example.modvideoll.utilities.Constants.*;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modvideoll.R;
import com.example.modvideoll.listeners.UsersListener;
import com.example.modvideoll.models.User;
import com.example.modvideoll.utilities.Constants;
import com.example.modvideoll.utilities.PreferenceManager;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User> users;
    private UsersListener usersListener;

    public UsersAdapter(List<User> users,UsersListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_user,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textFirstChar,textUsername, textEmail,WaitScreen;
        ImageView imageAudioMeeting, imageVideoMeeting;
         View viewDivider;


        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            textFirstChar = itemView.findViewById(R.id.textFirstChar);
            textUsername = itemView.findViewById(R.id.textUsername);
            textEmail = itemView.findViewById(R.id.textEmail);
            imageAudioMeeting = itemView.findViewById(R.id.imageAudioMeeting);
            imageVideoMeeting = itemView.findViewById(R.id.imageVideoMeeting);
            viewDivider = itemView.findViewById(R.id.viewDivider);
            WaitScreen = itemView.findViewById(R.id.WaitScreen);

        }
        void setUserData(User user){
            PreferenceManager preferenceManager;

            textFirstChar.setText(user.name.substring(0,1));
            textUsername.setText(user.name);
            textEmail.setText(user.permission);
           // WaitScreen.setVisibility(View.VISIBLE);

            setVisibility(user.selfPermission);


            imageAudioMeeting.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    usersListener.initiateAudiomeeting(user);
                }
            });
            imageVideoMeeting.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    usersListener.initiateVideomeeting(user);
                }
            });

        }
        void setVisibility(String permission){
            if(permission.equals(KEY_PERMISSION_PATIENT)){

                textFirstChar.setVisibility(View.INVISIBLE);
                textUsername.setVisibility(View.INVISIBLE);
                textEmail.setVisibility(View.INVISIBLE);
                viewDivider.setVisibility(View.INVISIBLE);
                imageVideoMeeting.setVisibility(View.INVISIBLE);
            }else{

                textFirstChar.setVisibility(View.VISIBLE);
                textUsername.setVisibility(View.VISIBLE);
                textEmail.setVisibility(View.VISIBLE);
                viewDivider.setVisibility(View.VISIBLE);
                imageVideoMeeting.setVisibility(View.VISIBLE);
               // WaitScreen.setVisibility(View.INVISIBLE);

            }

        }



    }
}
