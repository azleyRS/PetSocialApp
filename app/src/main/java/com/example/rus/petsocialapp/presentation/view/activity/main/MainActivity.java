package com.example.rus.petsocialapp.presentation.view.activity.main;

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
import com.example.rus.petsocialapp.presentation.presenter.MainPresenter;
import com.example.rus.petsocialapp.presentation.view.MainView;
import com.example.rus.petsocialapp.presentation.view.activity.profile.ProfileActivity;
import com.example.rus.petsocialapp.presentation.view.activity.signUp.SignUpActivity;

public class MainActivity extends AppCompatActivity implements MainView {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    TextView forgotPasswordLink, signUp;
    Button loginButton;

    //before Dagger2
    MainPresenter mainPresenter = new MainPresenter(new LoginInteractor());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter.attachView(this);

        initViews();
        setOcLickListeners();
    }

    private void setOcLickListeners() {
        signUp.setOnClickListener(v -> mainPresenter.signUp());
        loginButton.setOnClickListener(v -> mainPresenter.login(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()));
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);
        forgotPasswordLink = findViewById(R.id.forget_password_link);
        signUp = findViewById(R.id.textViewSignup);
        loginButton = findViewById(R.id.buttonLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.getCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    public void navigateToSignUpActivity() {
        startActivity(SignUpActivity.newIntent(this));
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
    public void success() {
        Intent intent = ProfileActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void fail(String s) {
        Toast.makeText(this, "Error occurred: " + s, Toast.LENGTH_LONG).show();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
