package com.example.rus.petsocialapp.presentation.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.model.Friends;
import com.example.rus.petsocialapp.presentation.view.activity.chat.ChatActivity;
import com.example.rus.petsocialapp.presentation.view.activity.personProfile.PersonProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllFriendsAdapter extends FirebaseRecyclerAdapter<Friends, AllFriendsAdapter.FriendsViewHolder> {


    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllFriendsAdapter(@NonNull FirebaseRecyclerOptions<Friends> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull Friends model) {
        final String usersIds = getRef(position).getKey();
        holder.data.setText("Friends since: " +model.getDate());

        FirebaseDatabase.getInstance().getReference().child("Users").child(usersIds).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String userName = dataSnapshot.child("displayName").getValue().toString();
                    String profileImage = dataSnapshot.child("photoUrl").getValue().toString();

                    holder.username.setText(userName);
                    Glide.with(context).load(profileImage).into(holder.imageView);

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]{
                                    userName + "'s Profile", "Send Message"
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Select options");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        Intent profileIntent = PersonProfileActivity.newIntent(context);
                                        profileIntent.putExtra("accountUserId", usersIds);
                                        context.startActivity(profileIntent);
                                    }
                                    if (which == 1){
                                        Intent chatIntent = ChatActivity.newIntent(context);
                                        chatIntent.putExtra("accountUserId", usersIds);
                                        chatIntent.putExtra("userName", userName);
                                        context.startActivity(chatIntent);
                                    }
                                }
                            });
                            builder.show();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_users_display_layout, parent, false);
        return new FriendsViewHolder(view);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView username;
        CircleImageView imageView;
        TextView data;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            username = itemView.findViewById(R.id.all_users_profile_user_name);
            imageView = itemView.findViewById(R.id.all_users_profile_image);
            data = itemView.findViewById(R.id.all_users_status);
        }

    }
}
