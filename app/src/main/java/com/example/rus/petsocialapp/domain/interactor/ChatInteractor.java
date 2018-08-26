package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;
import com.example.rus.petsocialapp.presentation.model.Messages;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ChatInteractor {
    public Single<String> getProfileImage(String accountUserId) {
        return FirebaseDataSource.getProfileImage(accountUserId);
    }

    public Single<String> sendMessage(String messageText, String messageREceiverRef, String saveCurrentDate, String saveCurrentTime) {
        return FirebaseDataSource.sendMessage(messageText, messageREceiverRef, saveCurrentDate, saveCurrentTime);
    }

    public Observable<Messages> fetchMessages(String accountUserId) {
        return FirebaseDataSource.fetchMessages(accountUserId);
    }
}
