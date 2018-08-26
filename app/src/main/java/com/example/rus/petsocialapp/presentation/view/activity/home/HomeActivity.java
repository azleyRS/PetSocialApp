package com.example.rus.petsocialapp.presentation.view.activity.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.presentation.view.fragment.details.DetailsFragment;
import com.example.rus.petsocialapp.presentation.view.fragment.friends.FriendsFragment;
import com.example.rus.petsocialapp.presentation.view.fragment.post.PostsFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        initViews();
        initOnClickListeners();
    }

    private void initOnClickListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.action_details:
                    selectedFragment = DetailsFragment.newInstance();
                    break;
                case R.id.action_messages:
                    selectedFragment = FriendsFragment.newInstance();
                    break;
                case R.id.action_posts:
                    selectedFragment = PostsFragment.newInstance();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsFragment()).commit();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }
}
