package com.example.rus.petsocialapp.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.model.Posts;
import com.example.rus.petsocialapp.presentation.view.activity.comment.CommentActivity;
import com.example.rus.petsocialapp.presentation.view.activity.post.PostActivity;
import com.example.rus.petsocialapp.presentation.view.activity.postEdit.PostEditActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends FirebaseRecyclerAdapter<Posts, PostsAdapter.PostsViewHolder> {

    private final Context contex;
    private String currentUserId;
    private boolean likeChecker = false;
    //check


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostsAdapter(@NonNull FirebaseRecyclerOptions<Posts> options, Context context) {
        super(options);
        this.contex = context;
        //check
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
        final String postKey = getRef(position).getKey();

        holder.username.setText(model.getFullname());
        Glide.with(contex).load(model.profileImage).into(holder.imageView);
        holder.postTime.setText(model.getTime());
        holder.postDate.setText(model.getDate());
        holder.postDescription.setText(model.getDescription());
        Glide.with(contex).load(model.postimage).into(holder.postImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent clickPostIntent = new Intent(getContext(), PostEditActivity.class);
                clickPostIntent.putExtra("PostKey", postKey);
                startActivity(clickPostIntent);*/
                Intent intent = PostEditActivity.newIntent(contex);
                intent.putExtra("PostKey", postKey);
                contex.startActivity(intent);
            }
        });

        holder.likePostButton.setOnClickListener(v -> {
            likeChecker = true;

            //check if it working
            DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (likeChecker == true){
                        if (dataSnapshot.child(postKey).hasChild(currentUserId)){
                            likesRef.child(postKey).child(currentUserId).removeValue();
                            likeChecker = false;
                        } else {
                            likesRef.child(postKey).child(currentUserId).setValue(true);
                            likeChecker = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });                    });

        holder.setLikeButtonStatus(postKey);

        holder.commentPostButton.setOnClickListener(v -> {
            Intent intent = CommentActivity.newIntent(contex);
            intent.putExtra("PostKey", postKey);
            contex.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_post_layout, parent, false);
        return new PostsViewHolder(view);    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username;
        CircleImageView imageView;
        TextView postTime;
        TextView postDate;
        TextView postDescription;
        ImageView postImage;
        ImageButton likePostButton, commentPostButton;
        TextView likesNumber;

        int countLikes;
        String currentUserId;
        DatabaseReference postlikesRef;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            username = itemView.findViewById(R.id.post_user_name);
            imageView = itemView.findViewById(R.id.post_profile_image);
            postTime = itemView.findViewById(R.id.post_time);
            postDate = itemView.findViewById(R.id.post_date);
            postDescription = itemView.findViewById(R.id.all_post_description);
            postImage = itemView.findViewById(R.id.all_post_image);
            likePostButton = itemView.findViewById(R.id.like_button);
            commentPostButton = itemView.findViewById(R.id.comment_button);
            likesNumber = itemView.findViewById(R.id.likes_number_text_view);

            postlikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        public void setLikeButtonStatus(final String postKey) {
            postlikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postKey).hasChild(currentUserId)){
                        countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                        likePostButton.setImageResource(R.drawable.like);
                        likesNumber.setText((Integer.toString(countLikes))+(" Likes"));
                    } else {
                        countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                        likePostButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                        likesNumber.setText((Integer.toString(countLikes))+(" Likes"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
