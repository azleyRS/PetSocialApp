package com.example.rus.petsocialapp.presentation.view.activity.post;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.PostsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.PostPresenter;
import com.example.rus.petsocialapp.presentation.view.PostView;
import com.example.rus.petsocialapp.presentation.view.activity.home.HomeActivity;

public class PostActivity extends AppCompatActivity implements PostView {

    private static final int GALLERY_PICK = 1001;
    //before Dagger2
    PostPresenter postPresenter = new PostPresenter(new PostsInteractor());

    private ProgressBar loadingBar;
    private Toolbar mToolbar;
    private ImageButton selectPostImage;
    private Button updatePostButton;
    private EditText postDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postPresenter.attachView(this);

        initViews();
        initOnClickListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data!=null){
            postPresenter.setImageUri(data.getData());
            selectPostImage.setImageURI(data.getData());
        }
    }

    private void initOnClickListeners() {
        selectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPresenter.openGallery();
            }
        });

        updatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPresenter.validateInfo(postDescription.getText().toString());
            }
        });
    }

    private void initViews() {
        selectPostImage = findViewById(R.id.select_post_image);
        updatePostButton = findViewById(R.id.update_post_button);
        postDescription = findViewById(R.id.post_description);
        loadingBar = findViewById(R.id.post_progress_bar);
        mToolbar = findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PostActivity.class);
        return intent;
    }

    @Override
    public void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_PICK);
    }

    @Override
    public void ImageIsEmpty() {
        Toast.makeText(this, "Please select post image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void descriptionIsEmpty() {
        postDescription.setError("Description is required");
        postDescription.requestFocus();
    }

    @Override
    public void success() {
        Intent intent = HomeActivity.newIntent(this);
        startActivity(intent);
    }

    @Override
    public void fail(String s) {
        Toast.makeText(this, "Error occured: " + s, Toast.LENGTH_SHORT).show();
    }
}
