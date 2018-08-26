package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

public class PostEditInteractor {

    public Single<Map<String,String>> getPost(String postKey) {
        return FirebaseDataSource.getPost(postKey);
    }

    public Single<String> editPost(String newDescription, String postKey) {
        return FirebaseDataSource.editPost(newDescription, postKey);
    }

    public Single<String> deletePost(String postKey, String imageUrl) {
        return FirebaseDataSource.deletePost(postKey, imageUrl);
    }
}
