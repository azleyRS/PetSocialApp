package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.PostEditInteractor;
import com.example.rus.petsocialapp.presentation.view.PostEditView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PostEditPresenter {

    private PostEditView postEditView;
    private PostEditInteractor postEditInteractor;

    private String imageUrl;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PostEditPresenter(PostEditInteractor postEditInteractor) {
        this.postEditInteractor = postEditInteractor;
    }

    public void attachView(PostEditView postsEditActivity) {
        postEditView = postsEditActivity;
    }

    public void detachView(){
        postEditView = null;
        compositeDisposable.dispose();
    }


    public void loadPost(String postKey) {
        Disposable disposable = postEditInteractor.getPost(postKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Map<String, String> s) -> {
                    String description = s.get("description");
                    String image = s.get("image");
                    imageUrl = image;

                    String currentUserId = s.get("currentUserId");
                    String databaseUserId = s.get("databaseUserId");
                    postEditView.loadPost(description, image);
                    if (currentUserId.equals(databaseUserId)){
                        postEditView.setButtonsVisible();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void editCurrentPost(String description) {
        postEditView.showDialog(description);
    }

    public void editPost(String newDescription, String postKey) {
        Disposable disposable = postEditInteractor.editPost(newDescription,postKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( s -> {
                    if (s.equals("success")){
                        postEditView.success(newDescription);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void deleteCurrentPost(String postKey) {
        Disposable disposable = postEditInteractor.deletePost(postKey, imageUrl)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        postEditView.postDeleted();
                    }
                });
        compositeDisposable.add(disposable);
    }
}
