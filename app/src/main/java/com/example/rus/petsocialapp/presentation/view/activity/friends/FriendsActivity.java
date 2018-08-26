package com.example.rus.petsocialapp.presentation.view.activity.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.FriendsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.FriendsPresenter;
import com.example.rus.petsocialapp.presentation.view.FriendsView;
import com.example.rus.petsocialapp.presentation.view.adapter.AllFriendsAdapter;

public class FriendsActivity extends AppCompatActivity implements FriendsView {

    //before dagger2
    FriendsPresenter friendsPresenter = new FriendsPresenter(new FriendsInteractor());

    private RecyclerView recyclerView;
    private AllFriendsAdapter allFriendsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsPresenter.attachView(this);

        initViews();
        displayFriends();
    }

    @Override
    protected void onPause() {
        super.onPause();
        allFriendsAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allFriendsAdapter.startListening();
    }

    private void displayFriends() {
        allFriendsAdapter = new AllFriendsAdapter(friendsPresenter.getOptions(),this);
        recyclerView.setAdapter(allFriendsAdapter);
        allFriendsAdapter.startListening();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.friends_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        friendsPresenter.detachView();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FriendsActivity.class);
    }
}
