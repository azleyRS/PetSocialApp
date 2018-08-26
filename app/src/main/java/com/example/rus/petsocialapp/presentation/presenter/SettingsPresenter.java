package com.example.rus.petsocialapp.presentation.presenter;

import android.net.Uri;

import com.example.rus.petsocialapp.domain.interactor.SettingsInteractor;
import com.example.rus.petsocialapp.presentation.view.SettingsView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsPresenter {

    private SettingsView settingsView;
    private SettingsInteractor settingsInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String storageUrl;

    public SettingsPresenter(SettingsInteractor settingsInteractor) {
        this.settingsInteractor = settingsInteractor;
    }

    public void attachView(SettingsView settingsActivity) {
        settingsView = settingsActivity;
    }

    public void detachView(){
        settingsView = null;
        compositeDisposable.dispose();
    }

    public void loadProfile() {
        Disposable disposable = settingsInteractor.getProfileInfo()
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

                    settingsView.loadProfile(myProfileImage, myProfileName, myProfileStatus, myProfileDOB, myProfileFavDrinks, myProfileGender, myProfileCity, myProfileCountry);
                });
        compositeDisposable.add(disposable);
    }

    public void onImageClick() {
        settingsView.showImageChooser();
    }

    public void uploadImageToFirebaseStorage(Uri data) {
        Disposable disposable = settingsInteractor.uploadImageToFirebaseStorage(data)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //something here
                    if (s.startsWith("Error")){
                        settingsView.error(s);
                    } else {
                        //here
                        storageUrl = s;
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void saveUserInformation(String userName, String usetStatus, String userDOB, String userFavDrinks, String userGender, String userCity, String userCountry) {
        if (userName.isEmpty()) {
            settingsView.userNameIsEmpty();
            return;
        }
        if (usetStatus.isEmpty()) {
            settingsView.usetStatusIsEmpty();
            return;
        }
        if (userDOB.isEmpty()) {
            settingsView.userDOBIsEmpty();
            return;
        }
        if (userFavDrinks.isEmpty()) {
            settingsView.userFavDrinksIsEmpty();
            return;
        }
        if (userGender.isEmpty()) {
            settingsView.userGenderIsEmpty();
            return;
        }
        if (userCity.isEmpty()) {
            settingsView.userCityIsEmpty();
            return;
        }
        if (userCountry.isEmpty()) {
            settingsView.userCountryIsEmpty();
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
        if (storageUrl != null){
            userMap.put("photoUrl", storageUrl);
        }
        Disposable disposable = settingsInteractor.createUserAccount(userMap)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        settingsView.success();
                    } else {
                        settingsView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);

    }
}
