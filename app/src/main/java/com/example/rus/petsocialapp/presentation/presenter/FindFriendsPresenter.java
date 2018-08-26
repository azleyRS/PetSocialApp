package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.FindFriendsInteractor;
import com.example.rus.petsocialapp.presentation.model.FindFriends;
import com.example.rus.petsocialapp.presentation.view.FindFriendsView;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.reactivex.disposables.CompositeDisposable;

public class FindFriendsPresenter {
    private FindFriendsView findFriendsView;
    private FindFriendsInteractor findFriendsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FindFriendsPresenter(FindFriendsInteractor findFriendsInteractor) {
        this.findFriendsInteractor = findFriendsInteractor;
    }

    public void attachView(FindFriendsView findFriendsActivity) {
        findFriendsView = findFriendsActivity;
    }

    public void detachView(){
        findFriendsView = null;
        compositeDisposable.dispose();
    }


    public FirebaseRecyclerOptions<FindFriends> getOptions(String searchTextInput) {
        return findFriendsInteractor.getOptions(searchTextInput);
    }
}
