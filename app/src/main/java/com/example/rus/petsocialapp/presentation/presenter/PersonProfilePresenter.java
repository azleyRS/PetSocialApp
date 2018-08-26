package com.example.rus.petsocialapp.presentation.presenter;

import com.example.rus.petsocialapp.domain.interactor.PersonProfileInteractor;
import com.example.rus.petsocialapp.presentation.view.PersonProfileView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PersonProfilePresenter {
    private PersonProfileView personProfileView;
    private PersonProfileInteractor personProfileInteractor;

    String recieverUserId;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PersonProfilePresenter(PersonProfileInteractor personProfileInteractor) {
        this.personProfileInteractor = personProfileInteractor;
    }

    public void attachView(PersonProfileView personProfileActivity) {
        personProfileView = personProfileActivity;
    }

    public void detachView(){
        personProfileView = null;
        compositeDisposable.dispose();
    }

    public void loadProfile(String recieverUserId) {
        this.recieverUserId = recieverUserId;
        Disposable disposable = personProfileInteractor.getProfileInfo(recieverUserId)
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

                    personProfileView.loadProfile(myProfileImage, myProfileName, myProfileStatus, myProfileDOB, myProfileFavDrinks, myProfileGender, myProfileCity, myProfileCountry);
                });
        compositeDisposable.add(disposable);
    }

    public void buttonBehavior() {
        Disposable disposable = personProfileInteractor.checkFriendshipStatus(recieverUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("sent")){
                        personProfileView.requestSent("request sent");
                    } else if (s.equals("received")){
                        personProfileView.requestReceived("request received");
                    } else if (s.equals("friends")){
                        personProfileView.requestFriends("friends");
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void cancelFriendRequest() {
        Disposable disposable = personProfileInteractor.cancelFriendRequest(recieverUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("not friends")){
                        personProfileView.requestNotFriends("not friends");
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void checkIfSenderIsReciever() {
        Disposable disposable = personProfileInteractor.getUserUid()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!s.equals(recieverUserId)){
                        //here
                        personProfileView.notSelf();
                    } else {
                        personProfileView.self();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void sendFriendRequest(String recieverUserId) {
        Disposable disposable = personProfileInteractor.sendFriendrequest(recieverUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("request sent")){
                        personProfileView.requestSent(s);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void acceptFriendRequest(String recieverUserId) {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentData.format(calForDate.getTime());

        Disposable disposable = personProfileInteractor.acceptFriendRequest(recieverUserId, saveCurrentDate)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //here
                    if (s.equals("friends")){
                        personProfileView.requestFriends(s);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void unfriend(String recieverUserId) {
        Disposable disposable = personProfileInteractor.unfriend(recieverUserId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //here
                    if (s.equals("not friends")){
                        personProfileView.requestNotFriends(s);
                    }
                });
        compositeDisposable.add(disposable);
    }
}
