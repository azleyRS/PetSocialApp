package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;
import com.example.rus.petsocialapp.presentation.model.Comments;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.reactivex.Single;

public class CommentsInteractor {
    public Single<String> addComment(String comment, String saveCurrentDate, String saveCurrentTime, String postKey) {
        return FirebaseDataSource.addComment(comment, saveCurrentDate, saveCurrentTime, postKey);
    }

    public FirebaseRecyclerOptions<Comments> getOptions(String postKey) {
        return FirebaseDataSource.getCommentsOptions(postKey);
    }
}
