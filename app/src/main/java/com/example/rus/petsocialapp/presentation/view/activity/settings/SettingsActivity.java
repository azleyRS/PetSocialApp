package com.example.rus.petsocialapp.presentation.view.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.SettingsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.SettingsPresenter;
import com.example.rus.petsocialapp.presentation.view.SettingsView;
import com.example.rus.petsocialapp.presentation.view.activity.home.HomeActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements SettingsView {

    private static final int GALLERY_PICK = 1001;
    //before Dagger2
    SettingsPresenter settingsPresenter = new SettingsPresenter(new SettingsInteractor());

    private Toolbar mToolbar;
    private EditText userName, usetStatus, userDOB, userFavDrinks, userGender, userCity, userCountre;
    private Button updateAccountSettingsButton;
    private CircleImageView userProfileImage;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPresenter.attachView(this);
        initViews();
        setOnClickListeners();

        settingsPresenter.loadProfile();
    }

    private void setOnClickListeners() {
        updateAccountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.saveUserInformation(userName.getText().toString(),
                        usetStatus.getText().toString(),
                        userDOB.getText().toString(),
                        userFavDrinks.getText().toString(),
                        userGender.getText().toString(),
                        userCity.getText().toString(),
                        userCountre.getText().toString());            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.onImageClick();
            }
        });
    }

    private void initViews() {
        mToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.settings_username);
        usetStatus = findViewById(R.id.settings_status);
        userDOB = findViewById(R.id.settings_dob);
        userFavDrinks = findViewById(R.id.settings_drinks);
        userGender = findViewById(R.id.settings_gender);
        userCity = findViewById(R.id.settings_city);
        userCountre = findViewById(R.id.settings_country);
        loadingBar = findViewById(R.id.settings_progress_bar);

        updateAccountSettingsButton = findViewById(R.id.update_account_settings_button);
        userProfileImage = findViewById(R.id.settings_profile_image);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingsPresenter.detachView();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    public void loadProfile(String myProfileImage, String myProfileName, String myProfileStatus, String myProfileDOB, String myProfileFavDrinks, String myProfileGender, String myProfileCity, String myProfileCountry) {
        Glide.with(SettingsActivity.this).load(myProfileImage).into(userProfileImage);
        userName.setText(myProfileName);
        usetStatus.setText(myProfileStatus);
        userDOB.setText(myProfileDOB);
        userFavDrinks.setText(myProfileFavDrinks);
        userGender.setText(myProfileGender);
        userCity.setText(myProfileCity);
        userCountre.setText(myProfileCountry);
    }

    @Override
    public void showImageChooser() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_PICK);
    }

    @Override
    public void error(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userNameIsEmpty() {
        userName.setError("Username is required");
        userName.requestFocus();
    }

    @Override
    public void usetStatusIsEmpty() {
        usetStatus.setError("User status is required");
        usetStatus.requestFocus();
    }

    @Override
    public void userDOBIsEmpty() {
        userDOB.setError("Day of Birth is required");
        userDOB.requestFocus();
    }

    @Override
    public void userFavDrinksIsEmpty() {
        userFavDrinks.setError("Your favorite drink is required");
        userFavDrinks.requestFocus();
    }

    @Override
    public void userGenderIsEmpty() {
        userGender.setError("Gender is required");
        userGender.requestFocus();
    }

    @Override
    public void userCityIsEmpty() {
        userCity.setError("City is required");
        userCity.requestFocus();
    }

    @Override
    public void userCountryIsEmpty() {
        userCountre.setError("Country is required");
        userCountre.requestFocus();
    }

    @Override
    public void imageIsEmpty() {
        Toast.makeText(this, "Please choose your image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        Intent intent = HomeActivity.newIntent(this);
        startActivity(intent);
    }

    @Override
    public void fail(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                userProfileImage.setImageBitmap(bitmap);

                settingsPresenter.uploadImageToFirebaseStorage(data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
