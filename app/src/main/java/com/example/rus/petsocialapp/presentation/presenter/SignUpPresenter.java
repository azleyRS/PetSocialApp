package com.example.rus.petsocialapp.presentation.presenter;

import android.util.Patterns;
import android.view.View;

import com.example.rus.petsocialapp.domain.interactor.LoginInteractor;
import com.example.rus.petsocialapp.presentation.view.SignUpView;
import com.example.rus.petsocialapp.presentation.view.activity.signUp.SignUpActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpPresenter {

    private SignUpView signUpView;
    private LoginInteractor loginInteractor;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SignUpPresenter(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void attachView(SignUpView signUpActivity) {
        signUpView = signUpActivity;
    }

    public void detachView(){
        signUpView = null;
        compositeDisposable.dispose();
    }

    public void registerUser(String email, String password) {
        if (email.isEmpty()) {
            signUpView.emailIsEmpty();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.emailIsNotValid();
            return;
        }

        if (password.isEmpty()) {
            signUpView.passwordIsEmpty();
            return;
        }

        if (password.length() < 6) {
            signUpView.passwordIsTooShort();
            return;
        }

        signUpView.showProgressBar();
        Disposable disposable = loginInteractor.createUser(email, password)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("success")){
                        signUpView.success();
                    } else {
                        signUpView.fail(s);
                    }
                    signUpView.hideProgressBar();
                });
        compositeDisposable.add(disposable);
    }
}
