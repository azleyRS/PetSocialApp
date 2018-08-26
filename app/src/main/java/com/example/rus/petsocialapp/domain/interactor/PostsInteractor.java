package com.example.rus.petsocialapp.domain.interactor;

import android.net.Uri;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;
import com.example.rus.petsocialapp.presentation.model.Posts;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.reactivex.Single;

public class PostsInteractor {
    public static FirebaseRecyclerOptions<Posts> getOptions() {
        return FirebaseDataSource.getPostOptions();
    }

    public Single<String> addPost(Uri imageUri, String description, String saveCurrentDate, String saveCurrentTime, long millis) {
        return FirebaseDataSource.addPost(imageUri, description, saveCurrentDate, saveCurrentTime, millis);
    }
}
