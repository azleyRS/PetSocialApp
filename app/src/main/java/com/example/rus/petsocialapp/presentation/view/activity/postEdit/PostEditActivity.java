package com.example.rus.petsocialapp.presentation.view.activity.postEdit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.PostEditInteractor;
import com.example.rus.petsocialapp.presentation.presenter.PostEditPresenter;
import com.example.rus.petsocialapp.presentation.view.PostEditView;
import com.example.rus.petsocialapp.presentation.view.activity.home.HomeActivity;

public class PostEditActivity extends AppCompatActivity implements PostEditView {

    private String postKey;

    private ImageView postImage;
    private TextView postDescription;
    private Button deletePostButton, editPostButton;

    //before Dagger2
    PostEditPresenter postEditPresenter = new PostEditPresenter(new PostEditInteractor());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        postKey = getIntent().getStringExtra("PostKey");
        postEditPresenter.attachView(this);

        initViews();
        setOnClickListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postEditPresenter.detachView();
    }

    private void setOnClickListeners() {
        deletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEditPresenter.deleteCurrentPost(postKey);
            }
        });

        editPostButton.setOnClickListener(v ->
                postEditPresenter.editCurrentPost(postDescription.getText().toString()));
    }

    private void initViews() {
        postImage = findViewById(R.id.edit_post_image);
        postDescription = findViewById(R.id.edit_post_description);
        deletePostButton = findViewById(R.id.dele_post_button);
        editPostButton = findViewById(R.id.edit_post_button);

        deletePostButton.setVisibility(View.INVISIBLE);
        editPostButton.setVisibility(View.INVISIBLE);
        postEditPresenter.loadPost(postKey);
    }

    public static Intent newIntent(Context contex) {
        return new Intent(contex, PostEditActivity.class);
    }

    @Override
    public void loadPost(String description, String image) {
        postDescription.setText(description);
        Glide.with(PostEditActivity.this).load(image).into(postImage);
    }

    @Override
    public void setButtonsVisible() {
        deletePostButton.setVisibility(View.VISIBLE);
        editPostButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialog(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Post: ");

        final EditText inputField = new EditText(PostEditActivity.this);
        inputField.setText(description);
        builder.setView(inputField);

        builder.setPositiveButton("Update", (dialog, which) -> postEditPresenter.editPost(inputField.getText().toString(), postKey));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        Dialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void success(String newDescription) {
        postDescription.setText(newDescription);
        Toast.makeText(PostEditActivity.this, "Post updated successfully...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void postDeleted() {
        Intent intent = HomeActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
