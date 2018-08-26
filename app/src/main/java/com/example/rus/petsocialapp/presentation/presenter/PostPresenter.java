package com.example.rus.petsocialapp.presentation.presenter;

import android.net.Uri;

import com.example.rus.petsocialapp.domain.interactor.PostsInteractor;
import com.example.rus.petsocialapp.presentation.view.PostView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PostPresenter {

    private PostView postView;
    private PostsInteractor postsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Uri imageUri;

    public PostPresenter(PostsInteractor postsInteractor) {
        this.postsInteractor = postsInteractor;
    }

    public void attachView(PostView postActivity) {
        postView = postActivity;
    }

    public void detachView(){
        postView = null;
        compositeDisposable.dispose();
    }

    public void openGallery() {
        postView.openGallery();
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void validateInfo(String description) {
        if (imageUri == null){
            postView.ImageIsEmpty();
            return;
        }
        if (description.isEmpty()) {
            postView.descriptionIsEmpty();
            return;
        }

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentData.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        long millis = calendar.getTimeInMillis();

        Disposable disposable = postsInteractor.addPost(imageUri, description, saveCurrentDate, saveCurrentTime, millis)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        postView.success();
                    } else {
                        postView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);

    }
}
