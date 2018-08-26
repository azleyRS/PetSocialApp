package com.example.rus.petsocialapp.presentation.view.activity.findFriends;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.FindFriendsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.FindFriendsPresenter;
import com.example.rus.petsocialapp.presentation.view.FindFriendsView;
import com.example.rus.petsocialapp.presentation.view.adapter.FriendsAdapter;

public class FindsFriendsActivity extends AppCompatActivity implements FindFriendsView {


    //before dagger2
    FindFriendsPresenter findFriendsPresenter = new FindFriendsPresenter(new FindFriendsInteractor());

    private Toolbar mToolbar;
    private ImageButton searchButton;
    private EditText searchInput;
    private RecyclerView recyclerView;

    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finds_friends);

        findFriendsPresenter.attachView(this);

        initViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        searchButton.setOnClickListener(v -> {

            friendsAdapter = new FriendsAdapter(findFriendsPresenter.getOptions(searchInput.getText().toString()), this);
            recyclerView.setAdapter(friendsAdapter);
            friendsAdapter.startListening();
            Toast.makeText(FindsFriendsActivity.this, "Searching", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        friendsAdapter.stopListening();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.find_friends_app_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

        recyclerView = findViewById(R.id.find_friends_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton = findViewById(R.id.find_friends_search_button);
        searchInput = findViewById(R.id.find_friends_search_edit_text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        findFriendsPresenter.detachView();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, FindsFriendsActivity.class);
        return intent;
    }
}
