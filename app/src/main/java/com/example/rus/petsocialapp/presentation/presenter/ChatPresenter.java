package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.ChatInteractor;
import com.example.rus.petsocialapp.presentation.view.ChatView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatPresenter {
    private ChatView chatView;
    private ChatInteractor chatInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ChatPresenter(ChatInteractor chatInteractor) {
        this.chatInteractor = chatInteractor;
    }

    public void attachView(ChatView chatActivity) {
        chatView = chatActivity;
    }

    public void detachView(){
        chatView = null;
        compositeDisposable.dispose();
    }

    public void displayReceiverInfo(String accountUserId) {
        Disposable disposable = chatInteractor.getProfileImage(accountUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    chatView.loadImage(s);
                });
        compositeDisposable.add(disposable);
    }

    public void sendMessage(String messageText, String messageREceiverRef) {
        if (messageText.isEmpty()) {
            chatView.MessageIsEmpty();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentData.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        Disposable disposable = chatInteractor.sendMessage(messageText, messageREceiverRef, saveCurrentDate, saveCurrentTime)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        chatView.success();
                    } else {
                        chatView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);

    }

    public void fetchMessages(String accountUserId) {
        Disposable disposable = chatInteractor.fetchMessages(accountUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    chatView.addMessage(s);
                });
        compositeDisposable.add(disposable);
    }
}
