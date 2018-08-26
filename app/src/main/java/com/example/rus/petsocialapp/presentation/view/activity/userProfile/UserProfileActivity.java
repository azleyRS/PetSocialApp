package com.example.rus.petsocialapp.presentation.view.activity.userProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.UserProfileInteractor;
import com.example.rus.petsocialapp.presentation.presenter.UserProfilePresenter;
import com.example.rus.petsocialapp.presentation.view.UserProfileView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements UserProfileView {

    private TextView userName, usetStatus, userDOB, userFavDrinks, userGender, userCity, userCountre;
    private CircleImageView userProfileUmage;

    //before Dagger2
    UserProfilePresenter userProfilePresenter = new UserProfilePresenter(new UserProfileInteractor());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfilePresenter.attachView(this);

        initViews();

        userProfilePresenter.loadProfile();
    }

    private void initViews() {
        userName = findViewById(R.id.my_profile_username);
        usetStatus = findViewById(R.id.my_profile_status);
        userDOB = findViewById(R.id.my_profile_dob);
        userFavDrinks = findViewById(R.id.my_profile_fav_drinks);
        userGender = findViewById(R.id.my_profile_gender);
        userCity = findViewById(R.id.my_profile_city);
        userCountre = findViewById(R.id.my_profile_country);
        userProfileUmage = findViewById(R.id.my_profile_picture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfilePresenter.detachView();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        return intent;
    }

    @Override
    public void loadProfile(String myProfileImage, String myProfileName, String myProfileStatus, String myProfileDOB, String myProfileFavDrinks, String myProfileGender, String myProfileCity, String myProfileCountry) {
        Glide.with(UserProfileActivity.this).load(myProfileImage).into(userProfileUmage);
        userName.setText(myProfileName);
        usetStatus.setText(myProfileStatus);
        userDOB.setText(myProfileDOB);
        userFavDrinks.setText(myProfileFavDrinks);
        userGender.setText(myProfileGender);
        userCity.setText(myProfileCity);
        userCountre.setText(myProfileCountry);
    }
}
