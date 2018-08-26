package com.example.rus.petsocialapp.presentation.presenter;

import android.util.Patterns;

import com.example.rus.petsocialapp.domain.interactor.LoginInteractor;
import com.example.rus.petsocialapp.presentation.view.MainView;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    private MainView mainView;
    private LoginInteractor loginInteractor;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void attachView(MainView mainActivity) {
        mainView = mainActivity;
    }

    public void detachView(){
        mainView = null;
        compositeDisposable.dispose();
    }

    public void signUp() {
        mainView.navigateToSignUpActivity();
    }

    public void login(String email, String password) {
        if (email.isEmpty()) {
            mainView.emailIsEmpty();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mainView.emailIsNotValid();
            return;
        }

        if (password.isEmpty()) {
            mainView.passwordIsEmpty();
            return;
        }

        if (password.length() < 6) {
            mainView.passwordIsTooShort();
            return;
        }
        Disposable disposable = loginInteractor.login(email, password)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        mainView.success();
                    } else {
                        mainView.fail(s);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getCurrentUser() {
        Disposable disposable = loginInteractor.getCurrentUser()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        mainView.success();
                    }
                });
        compositeDisposable.add(disposable);
    }
}
