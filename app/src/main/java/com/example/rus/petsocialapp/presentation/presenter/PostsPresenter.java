package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.PostsInteractor;
import com.example.rus.petsocialapp.presentation.model.Posts;
import com.example.rus.petsocialapp.presentation.view.PostsView;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.reactivex.disposables.CompositeDisposable;

public class PostsPresenter {

    private PostsView postsView;
    private PostsInteractor postsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PostsPresenter(PostsInteractor postsInteractor) {
        this.postsInteractor = postsInteractor;
    }

    public void attachView(PostsView postsActivity) {
        postsView = postsActivity;
    }

    public void detachView(){
        postsView = null;
        compositeDisposable.dispose();
    }


    public FirebaseRecyclerOptions<Posts> getOptions() {
        return PostsInteractor.getOptions();
    }

    public void addPost() {
        postsView.addPost();
    }
}
