package com.example.rus.petsocialapp.presentation.view;

public interface PersonProfileView {
    void loadProfile(String myProfileImage, String myProfileName, String myProfileStatus, String myProfileDOB, String myProfileFavDrinks, String myProfileGender, String myProfileCity, String myProfileCountry);

    void requestSent(String request_sent);

    void requestReceived(String request_received);

    void requestFriends(String friends);

    void requestNotFriends(String not_friends);

    void notSelf();

    void self();
}
