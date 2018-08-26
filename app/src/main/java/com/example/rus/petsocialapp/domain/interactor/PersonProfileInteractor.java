package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;

import java.util.Map;

import io.reactivex.Single;

public class PersonProfileInteractor {
    public Single<Map<String,String>> getProfileInfo(String recieverUserId) {
        return FirebaseDataSource.getProfileInfo(recieverUserId);
    }

    public Single<String> checkFriendshipStatus(String recieverUserId) {
        return FirebaseDataSource.checFriendshipStatus(recieverUserId);
    }

    public Single<String> cancelFriendRequest(String recieverUserId) {
        return FirebaseDataSource.cancelFriendRequest(recieverUserId);
    }

    public Single<String> getUserUid() {
        return FirebaseDataSource.getCurrentUserUid();
    }

    public Single<String> sendFriendrequest(String recieverUserId) {
        return FirebaseDataSource.sendFriendRequest(recieverUserId);
    }

    public Single<String> acceptFriendRequest(String recieverUserId, String saveCurrentDate) {
        return  FirebaseDataSource.acceptFriendRequest(recieverUserId, saveCurrentDate);
    }

    public Single<String> unfriend(String recieverUserId) {
        return FirebaseDataSource.unfriend(recieverUserId);
    }
}
