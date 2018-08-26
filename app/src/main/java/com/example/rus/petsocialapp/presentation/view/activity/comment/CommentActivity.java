package com.example.rus.petsocialapp.presentation.view.activity.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.CommentsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.CommentsPresenter;
import com.example.rus.petsocialapp.presentation.view.CommentsView;
import com.example.rus.petsocialapp.presentation.view.adapter.CommentsAdapter;

public class CommentActivity extends AppCompatActivity implements CommentsView {

    private ImageButton postCommentButton;
    private EditText commnetInput;
    private RecyclerView recyclerView;

    //before Dagger2
    CommentsPresenter commentsPresenter = new CommentsPresenter(new CommentsInteractor());
    private String postKey;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postKey = getIntent().getStringExtra("PostKey");

        commentsPresenter.attachView(this);

        initViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        postCommentButton.setOnClickListener(v -> commentsPresenter.addComment(commnetInput.getText().toString(), postKey));
    }

    private void initViews() {
        postCommentButton = findViewById(R.id.comment_post_button);
        commnetInput = findViewById(R.id.comment_edit_text);
        recyclerView = findViewById(R.id.comment_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayComments();
    }

    private void displayComments() {
        commentsAdapter = new CommentsAdapter(commentsPresenter.getOptions(postKey));
        recyclerView.setAdapter(commentsAdapter);
        commentsAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentsPresenter.detachView();
    }

    @Override
    public void onStart() {
        super.onStart();
        commentsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        commentsAdapter.stopListening();
    }

    public static Intent newIntent(Context contex) {
        return new Intent(contex, CommentActivity.class);
    }

    @Override
    public void commentIsEmpty() {
        commnetInput.setError("Comment is required");
        commnetInput.requestFocus();
    }

    @Override
    public void success() {
        Toast.makeText(this, "You have commented successfully", Toast.LENGTH_SHORT).show();
        commnetInput.setText("");
    }
}
