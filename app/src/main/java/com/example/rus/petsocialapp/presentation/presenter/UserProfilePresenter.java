package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.UserProfileInteractor;
import com.example.rus.petsocialapp.presentation.view.UserProfileView;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserProfilePresenter {
    private UserProfileView userProfileView;
    private UserProfileInteractor userProfileInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UserProfilePresenter(UserProfileInteractor userProfileInteractor) {
        this.userProfileInteractor = userProfileInteractor;
    }

    public void attachView(UserProfileView userProfileActivity) {
        userProfileView = userProfileActivity;
    }

    public void detachView(){
        userProfileView = null;
        compositeDisposable.dispose();
    }

    public void loadProfile() {
        Disposable disposable = userProfileInteractor.getProfileInfo()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Map<String, String> s) -> {
                    String myProfileImage = s.get("photoUrl");
                    String myProfileName = s.get("displayName");
                    String myProfileStatus = s.get("status");
                    String myProfileDOB = s.get("dob");
                    String myProfileFavDrinks = s.get("drinks");
                    String myProfileGender = s.get("gender");
                    String myProfileCity = s.get("city");
                    String myProfileCountry = s.get("country");

                    userProfileView.loadProfile(myProfileImage, myProfileName, myProfileStatus, myProfileDOB, myProfileFavDrinks, myProfileGender, myProfileCity, myProfileCountry);
                });
        compositeDisposable.add(disposable);
    }
}
