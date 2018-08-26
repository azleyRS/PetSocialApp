package com.example.rus.petsocialapp.presentation.view;

public interface SettingsView {
    void loadProfile(String myProfileImage, String myProfileName, String myProfileStatus, String myProfileDOB, String myProfileFavDrinks, String myProfileGender, String myProfileCity, String myProfileCountry);

    void showImageChooser();

    void error(String s);

    void userNameIsEmpty();

    void usetStatusIsEmpty();

    void userDOBIsEmpty();

    void userFavDrinksIsEmpty();

    void userGenderIsEmpty();

    void userCityIsEmpty();

    void userCountryIsEmpty();

    void imageIsEmpty();

    void success();

    void fail(String s);
}
