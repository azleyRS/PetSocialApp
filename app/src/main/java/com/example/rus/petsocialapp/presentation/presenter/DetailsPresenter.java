package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.presentation.view.DetailedView;

public class DetailsPresenter {

    private DetailedView detailedView;
    //private DetailedInteractor detailedInteractor;

    //CompositeDisposable compositeDisposable = new CompositeDisposable();


    /*public DetailsPresenter(DetailedInteractor detailedInteractor) {
        this.detailedInteractor = detailedInteractor;
    }*/

    public void attachView(DetailedView detailedActivity) {
        detailedView = detailedActivity;
    }

    public void detachView(){
        detailedView = null;
        //compositeDisposable.dispose();
    }

    public void settingsClick() {
        detailedView.settings();
    }

    public void userProfileClick() { detailedView.userProfile();
    }

    public void findFriendsClick() { detailedView.findFriends();
    }

    public void friendsClick() { detailedView.allFriends();
    }
}
