package com.example.rus.petsocialapp.presentation.view;

public interface MainView {
    void navigateToSignUpActivity();

    void emailIsEmpty();

    void emailIsNotValid();

    void passwordIsEmpty();

    void passwordIsTooShort();

    void success();

    void fail(String s);
}
