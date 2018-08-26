package com.example.rus.petsocialapp.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.model.FindFriends;
import com.example.rus.petsocialapp.presentation.view.activity.personProfile.PersonProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends FirebaseRecyclerAdapter<FindFriends, FriendsAdapter.FindFriendsViewHolder> {

    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FriendsAdapter(@NonNull FirebaseRecyclerOptions<FindFriends> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFriends model) {
        holder.username.setText(model.getDisplayName());
        holder.status.setText(model.getStatus());
        Glide.with(context).load(model.getPhotoUrl()).into(holder.imageView);
        holder.mView.setOnClickListener(v -> {
            String accountUserId = getRef(position).getKey();
            Intent intent = PersonProfileActivity.newIntent(context);
            intent.putExtra("accountUserId", accountUserId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_users_display_layout, parent, false);
        return new FindFriendsViewHolder(view);    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username;
        CircleImageView imageView;
        TextView status;

        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            username = itemView.findViewById(R.id.all_users_profile_user_name);
            imageView = itemView.findViewById(R.id.all_users_profile_image);
            status = itemView.findViewById(R.id.all_users_status);
        }
    }

}
