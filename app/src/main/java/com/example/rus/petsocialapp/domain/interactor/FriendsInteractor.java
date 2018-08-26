package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;
import com.example.rus.petsocialapp.presentation.model.Friends;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FriendsInteractor {
    public FirebaseRecyclerOptions<Friends> getOptions() {
        return FirebaseDataSource.getAllFriendsOptions();
    }
}
