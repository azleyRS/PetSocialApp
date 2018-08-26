package com.example.rus.petsocialapp.presentation.presenter;

import android.net.Uri;

import com.example.rus.petsocialapp.domain.interactor.ProfileInteractor;
import com.example.rus.petsocialapp.presentation.view.ProfileView;
import com.example.rus.petsocialapp.presentation.view.activity.profile.ProfileActivity;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenter {
    private ProfileView profileView;
    private ProfileInteractor profileInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String storageUrl;

    public ProfilePresenter(ProfileInteractor profileInteractor) {
        this.profileInteractor = profileInteractor;
    }

    public void attachView(ProfileView profileActivity) {
        profileView = profileActivity;
    }

    public void detachView() {
        profileView = null;
        compositeDisposable.dispose();
    }

    public void onImageClick() {
        profileView.showImageChooser();
    }

    public void uploadImageToFirebaseStorage(Uri data) {
        Disposable disposable = profileInteractor.uploadImageToFirebaseStorage(data)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //something here
                    if (s.startsWith("Error")){
                        profileView.error(s);
                    } else {
                      //here
                      storageUrl = s;
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void saveUserInformation(String userName, String usetStatus, String userDOB, String userFavDrinks, String userGender, String userCity, String userCountry) {
        if (userName.isEmpty()) {
            profileView.userNameIsEmpty();
            return;
        }
        if (usetStatus.isEmpty()) {
            profileView.usetStatusIsEmpty();
            return;
        }
        if (userDOB.isEmpty()) {
            profileView.userDOBIsEmpty();
            return;
        }
        if (userFavDrinks.isEmpty()) {
            profileView.userFavDrinksIsEmpty();
            return;
        }
        if (userGender.isEmpty()) {
            profileView.userGenderIsEmpty();
            return;
        }
        if (userCity.isEmpty()) {
            profileView.userCityIsEmpty();
            return;
        }
        if (userCountry.isEmpty()) {
            profileView.userCountryIsEmpty();
            return;
        }
        if (storageUrl.isEmpty()){
            profileView.imageIsEmpty();
            return;
        }
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("displayName", userName);
        userMap.put("status", usetStatus);
        userMap.put("dob", userDOB);
        userMap.put("drinks", userFavDrinks);
        userMap.put("gender", userGender);
        userMap.put("city", userCity);
        userMap.put("country", userCountry);
        userMap.put("photoUrl", storageUrl);

        Disposable disposable = profileInteractor.createUserAccount(userMap)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        profileView.success();
                    } else {
                        profileView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);

    }

    public void checkCurrentUser() {
        Disposable disposable = profileInteractor.checkUserAccountExist()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        profileView.success();
                    }
                });
        compositeDisposable.add(disposable);
    }
}
