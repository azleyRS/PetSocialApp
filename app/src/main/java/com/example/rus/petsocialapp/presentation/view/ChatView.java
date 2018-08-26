package com.example.rus.petsocialapp.presentation.view;

import com.example.rus.petsocialapp.presentation.model.Messages;

public interface ChatView {
    void loadImage(String s);

    void MessageIsEmpty();

    void success();

    void fail(String s);

    void addMessage(Messages s);
}
