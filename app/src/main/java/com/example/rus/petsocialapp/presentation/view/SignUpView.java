package com.example.rus.petsocialapp.presentation.view;

public interface SignUpView {
    void emailIsEmpty();

    void emailIsNotValid();

    void passwordIsEmpty();

    void passwordIsTooShort();

    void showProgressBar();

    void success();

    void fail(String s);

    void hideProgressBar();
}
