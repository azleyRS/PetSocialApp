package com.example.rus.petsocialapp.presentation.view.fragment.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.presenter.DetailsPresenter;
import com.example.rus.petsocialapp.presentation.view.DetailedView;
import com.example.rus.petsocialapp.presentation.view.PostsView;
import com.example.rus.petsocialapp.presentation.view.activity.findFriends.FindsFriendsActivity;
import com.example.rus.petsocialapp.presentation.view.activity.friends.FriendsActivity;
import com.example.rus.petsocialapp.presentation.view.activity.settings.SettingsActivity;
import com.example.rus.petsocialapp.presentation.view.activity.userProfile.UserProfileActivity;

public class DetailsFragment extends Fragment implements DetailedView{

    // before dagger2
    DetailsPresenter detailsPresenter = new DetailsPresenter();

    private Button settingsButton, profileButton, findFriendsButton, friendsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailsPresenter.attachView(this);

        initViews(view);
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        settingsButton.setOnClickListener(v -> {
            detailsPresenter.settingsClick();
        });

        profileButton.setOnClickListener(v -> {
            detailsPresenter.userProfileClick();
        });

        findFriendsButton.setOnClickListener(v -> {
            detailsPresenter.findFriendsClick();
        });

        friendsButton.setOnClickListener(v -> {
            detailsPresenter.friendsClick();
        });
    }

    private void initViews(View view) {
        settingsButton = view.findViewById(R.id.settings_button);
        profileButton = view.findViewById(R.id.user_profile_button);
        findFriendsButton = view.findViewById(R.id.find_friends_button);
        friendsButton = view.findViewById(R.id.friends_button);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detailsPresenter.detachView();
    }

    public static Fragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void settings() {
        Intent intent = SettingsActivity.newIntent(getActivity());
        getActivity().startActivity(intent);
    }

    @Override
    public void userProfile() {
        Intent intent = UserProfileActivity.newIntent(getActivity());
        getActivity().startActivity(intent);
    }

    @Override
    public void findFriends() {
        Intent intent = FindsFriendsActivity.newIntent(getActivity());
        getActivity().startActivity(intent);
    }

    @Override
    public void allFriends() {
        Intent intent = FriendsActivity.newIntent(getActivity());
        getActivity().startActivity(intent);
    }
}
