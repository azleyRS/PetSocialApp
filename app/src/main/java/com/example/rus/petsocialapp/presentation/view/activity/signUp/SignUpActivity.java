package com.example.rus.petsocialapp.presentation.view.activity.signUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.LoginInteractor;
import com.example.rus.petsocialapp.presentation.presenter.SignUpPresenter;
import com.example.rus.petsocialapp.presentation.view.SignUpView;
import com.example.rus.petsocialapp.presentation.view.activity.main.MainActivity;
import com.example.rus.petsocialapp.presentation.view.activity.profile.ProfileActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    //before Dagger2
    SignUpPresenter signUpPresenter = new SignUpPresenter(new LoginInteractor());


    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword;
    Button signUpButton;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpPresenter.attachView(this);

        initViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        signUpButton.setOnClickListener(v -> {
            signUpPresenter.registerUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
        });
        loginTextView.setOnClickListener(v -> {
            startActivity(MainActivity.newIntent(this));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signUpPresenter.detachView();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.su_editTextEmail);
        editTextPassword = findViewById(R.id.su_editTextPassword);
        progressBar =  findViewById(R.id.su_progressbar);
        signUpButton = findViewById(R.id.su_buttonSignUp);
        loginTextView = findViewById(R.id.su_textViewLogin);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    public void emailIsEmpty() {
        editTextEmail.setError("Email is required");
        editTextEmail.requestFocus();
    }

    @Override
    public void emailIsNotValid() {
        editTextEmail.setError("Please enter a valid email");
        editTextEmail.requestFocus();
    }

    @Override
    public void passwordIsEmpty() {
        editTextPassword.setError("Password is required");
        editTextPassword.requestFocus();
    }

    @Override
    public void passwordIsTooShort() {
        editTextPassword.setError("Minimum length of password should be 6");
        editTextPassword.requestFocus();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void success() {
        Intent intent = ProfileActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void fail(String s) {
        Toast.makeText(this, "Error occured: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
