package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;


import java.util.Map;

import io.reactivex.Single;

public class UserProfileInteractor {
    public Single<Map<String,String>> getProfileInfo() {
        return FirebaseDataSource.getProfileInfo();
    }
}
