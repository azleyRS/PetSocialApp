package com.example.rus.petsocialapp.presentation.view;

public interface PostEditView {
    void loadPost(String description, String image);

    void setButtonsVisible();

    void showDialog(String description);


    void success(String newDescription);

    void postDeleted();
}
