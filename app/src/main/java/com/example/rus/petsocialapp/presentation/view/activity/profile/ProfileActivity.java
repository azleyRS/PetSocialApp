package com.example.rus.petsocialapp.presentation.view.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.ProfileInteractor;
import com.example.rus.petsocialapp.presentation.presenter.ProfilePresenter;
import com.example.rus.petsocialapp.presentation.view.ProfileView;
import com.example.rus.petsocialapp.presentation.view.activity.home.HomeActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    private static final int CHOOSE_IMAGE = 1001;
    CircleImageView circleImageView;

    private EditText userName, usetStatus, userDOB, userFavDrinks, userGender, userCity, userCountry;
    private ProgressBar loadingBar;
    Button updateAccountButton;

    //before Dagger2
    ProfilePresenter profilePresenter = new ProfilePresenter(new ProfileInteractor());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePresenter.attachView(this);

        initViews();
        setOnClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        profilePresenter.checkCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profilePresenter.detachView();
    }

    private void setOnClickListeners() {
        circleImageView.setOnClickListener(v -> {
            profilePresenter.onImageClick();
        });
        updateAccountButton.setOnClickListener(v -> {
            profilePresenter.saveUserInformation(userName.getText().toString(),
                    usetStatus.getText().toString(),
                    userDOB.getText().toString(),
                    userFavDrinks.getText().toString(),
                    userGender.getText().toString(),
                    userCity.getText().toString(),
                    userCountry.getText().toString());
        });
    }

    private void initViews() {
        userName = findViewById(R.id.profile_username);
        usetStatus = findViewById(R.id.profile_status);
        userDOB = findViewById(R.id.profile_dob);
        userFavDrinks = findViewById(R.id.profile_drinks);
        userGender = findViewById(R.id.profile_gender);
        userCity = findViewById(R.id.profile_city);
        userCountry = findViewById(R.id.profile_country);
        loadingBar = findViewById(R.id.profile_progress_bar);
        updateAccountButton = findViewById(R.id.update_account_profile_button);
        circleImageView = findViewById(R.id.profile_profile_image);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    public void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
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
        userCountry.setError("Country is required");
        userCountry.requestFocus();
    }

    @Override
    public void imageIsEmpty() {
        Toast.makeText(this, "Please choose your image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        Intent intent = HomeActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void fail(String s) {
        Toast.makeText(this, "Error occured: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                circleImageView.setImageBitmap(bitmap);

                profilePresenter.uploadImageToFirebaseStorage(data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
