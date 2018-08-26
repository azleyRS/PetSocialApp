package com.example.rus.petsocialapp.domain.interactor;

import android.net.Uri;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

public class SettingsInteractor {
    public Single<Map<String,String>> getProfileInfo() {
        return FirebaseDataSource.getProfileInfo();
    }

    public Single<String> uploadImageToFirebaseStorage(Uri data) {
        return FirebaseDataSource.uploadImageToFirebaseStorage(data);
    }

    public Single<String> createUserAccount(HashMap<String, Object> userMap) {
        return FirebaseDataSource.updateUserAccount(userMap);
    }
}
