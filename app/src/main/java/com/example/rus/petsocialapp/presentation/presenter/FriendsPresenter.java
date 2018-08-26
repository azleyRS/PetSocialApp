package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.FriendsInteractor;
import com.example.rus.petsocialapp.domain.interactor.PostsInteractor;
import com.example.rus.petsocialapp.presentation.model.Friends;
import com.example.rus.petsocialapp.presentation.view.FriendsView;
import com.example.rus.petsocialapp.presentation.view.PostsView;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.reactivex.disposables.CompositeDisposable;

public class FriendsPresenter {

    private FriendsView friendsView;
    private FriendsInteractor friendsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FriendsPresenter(FriendsInteractor friendsInteractor) {
        this.friendsInteractor = friendsInteractor;
    }

    public void attachView(FriendsView friendsActivity) {
        friendsView = friendsActivity;
    }

    public void detachView(){
        friendsView = null;
        compositeDisposable.dispose();
    }

    public FirebaseRecyclerOptions<Friends> getOptions() {
        return friendsInteractor.getOptions();
    }
}
