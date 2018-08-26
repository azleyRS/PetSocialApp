package com.example.rus.petsocialapp.presentation.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.model.Comments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CommentsAdapter extends FirebaseRecyclerAdapter<Comments, CommentsAdapter.CommentsViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CommentsAdapter(@NonNull FirebaseRecyclerOptions<Comments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model) {
        holder.commentText.setText(model.getComment());
        holder.userName.setText(model.getUsername());
        holder.dateText.setText(model.getDate());
        holder.timeText.setText(model.getTime());
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_comments_layout, parent, false);
        return new CommentsViewHolder(view);    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView commentText, userName, dateText, timeText;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            commentText = itemView.findViewById(R.id.comment_view_comment_text);
            userName = itemView.findViewById(R.id.comment_view_username);
            dateText = itemView.findViewById(R.id.comment_view_date_text_view);
            timeText = itemView.findViewById(R.id.comment_view_time_text_view);
        }


    }
}
