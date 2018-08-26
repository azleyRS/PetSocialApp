package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.CommentsInteractor;
import com.example.rus.petsocialapp.presentation.model.Comments;
import com.example.rus.petsocialapp.presentation.view.CommentsView;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentsPresenter {


    private CommentsView commentsView;
    private CommentsInteractor commentsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CommentsPresenter(CommentsInteractor commentsInteractor) {
        this.commentsInteractor = commentsInteractor;
    }

    public void attachView(CommentsView commentsActivity) {
        commentsView = commentsActivity;
    }

    public void detachView(){
        commentsView = null;
        compositeDisposable.dispose();
    }

    public void addComment(String comment, String postKey) {
        if (comment.isEmpty()) {
            commentsView.commentIsEmpty();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentData.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        Disposable disposable = commentsInteractor.addComment(comment, saveCurrentDate,saveCurrentTime, postKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        commentsView.success();
                    } else {
                        //mainView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public FirebaseRecyclerOptions<Comments> getOptions(String postKey) {
        return commentsInteractor.getOptions(postKey);
    }
}
