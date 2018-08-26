package com.example.rus.petsocialapp.presentation.view;

public interface ProfileView {
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
