package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class LoginInteractor {
    public Single<String> login(String email, String password) {
        return FirebaseDataSource.login(email, password);
    }

    public Single<String> getCurrentUser() {
        return FirebaseDataSource.getCurrentUser();
    }

    public Single<String> createUser(String email, String password) {
        return FirebaseDataSource.createUser(email, password);
    }
}
