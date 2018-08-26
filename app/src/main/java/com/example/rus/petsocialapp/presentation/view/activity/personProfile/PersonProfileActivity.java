package com.example.rus.petsocialapp.presentation.view.activity.personProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.PersonProfileInteractor;
import com.example.rus.petsocialapp.presentation.presenter.PersonProfilePresenter;
import com.example.rus.petsocialapp.presentation.view.PersonProfileView;
import com.example.rus.petsocialapp.presentation.view.activity.userProfile.UserProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity implements PersonProfileView {

    private TextView userName, usetStatus, userDOB, userFavDrinks, userGender, userCity, userCountre;
    private CircleImageView userProfileUmage;
    private Button sendFriendRequestButton, declineFriendRequestButton;

    //before Dagger2
    PersonProfilePresenter personProfilePresenter = new PersonProfilePresenter(new PersonProfileInteractor());
    private String CURRENT_STATE,recieverUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        recieverUserId = getIntent().getStringExtra("accountUserId");
        personProfilePresenter.attachView(this);

        initViews();

        personProfilePresenter.loadProfile(recieverUserId);

        declineFriendRequestButton.setVisibility(View.INVISIBLE);
        declineFriendRequestButton.setEnabled(false);

        personProfilePresenter.checkIfSenderIsReciever();
    }

    private void initViews() {
        userName = findViewById(R.id.person_profile_username);
        usetStatus = findViewById(R.id.person_profile_status);
        userDOB = findViewById(R.id.person_profile_dob);
        userFavDrinks = findViewById(R.id.person_profile_fav_drinks);
        userGender = findViewById(R.id.person_profile_gender);
        userCity = findViewById(R.id.person_profile_city);
        userCountre = findViewById(R.id.person_profile_country);
        userProfileUmage = findViewById(R.id.person_profile_picture);
        sendFriendRequestButton = findViewById(R.id.person_send_friend_request_button);
        declineFriendRequestButton = findViewById(R.id.person_decline_friend_request_button);

        CURRENT_STATE = "not friends";
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PersonProfileActivity.class);
        return intent;
    }

    @Override
    public void loadProfile(String myProfileImage, String myProfileName, String myProfileStatus, String myProfileDOB, String myProfileFavDrinks, String myProfileGender, String myProfileCity, String myProfileCountry) {
        Glide.with(PersonProfileActivity.this).load(myProfileImage).into(userProfileUmage);
        userName.setText(myProfileName);
        usetStatus.setText(myProfileStatus);
        userDOB.setText(myProfileDOB);
        userFavDrinks.setText(myProfileFavDrinks);
        userGender.setText(myProfileGender);
        userCity.setText(myProfileCity);
        userCountre.setText(myProfileCountry);

        personProfilePresenter.buttonBehavior();
    }

    @Override
    public void requestSent(String request_sent) {
        CURRENT_STATE = request_sent;
        sendFriendRequestButton.setText("Cancel Friend request");

        declineFriendRequestButton.setVisibility(View.INVISIBLE);
        declineFriendRequestButton.setEnabled(false);
    }

    @Override
    public void requestReceived(String request_received) {
        CURRENT_STATE = request_received;
        sendFriendRequestButton.setText("Accept Friend Request");

        declineFriendRequestButton.setVisibility(View.VISIBLE);
        declineFriendRequestButton.setEnabled(true);

        declineFriendRequestButton.setOnClickListener(v -> personProfilePresenter.cancelFriendRequest());
    }

    @Override
    public void requestFriends(String friends) {
        CURRENT_STATE = friends;
        sendFriendRequestButton.setText("Unfriend");

        declineFriendRequestButton.setVisibility(View.INVISIBLE);
        declineFriendRequestButton.setEnabled(false);
    }

    @Override
    public void requestNotFriends(String not_friends) {
        sendFriendRequestButton.setEnabled(true);
        CURRENT_STATE = not_friends;
        sendFriendRequestButton.setText("Send Friend Request");

        declineFriendRequestButton.setVisibility(View.INVISIBLE);
        declineFriendRequestButton.setEnabled(false);
    }

    @Override
    public void notSelf() {
        sendFriendRequestButton.setOnClickListener(v -> {
            sendFriendRequestButton.setEnabled(false);

            if (CURRENT_STATE.equals("not friends")){
                personProfilePresenter.sendFriendRequest(recieverUserId);
            }
            if (CURRENT_STATE.equals("request sent")){
                personProfilePresenter.cancelFriendRequest();
            }
            if (CURRENT_STATE.equals("request received")){
                personProfilePresenter.acceptFriendRequest(recieverUserId);
            }
            if (CURRENT_STATE.equals("friends")){
                personProfilePresenter.unfriend(recieverUserId);
            }
        });
    }

    @Override
    public void self() {
        sendFriendRequestButton.setVisibility(View.INVISIBLE);
        sendFriendRequestButton.setEnabled(false);
    }
}
