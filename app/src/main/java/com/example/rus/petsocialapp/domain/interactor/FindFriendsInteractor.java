package com.example.rus.petsocialapp.domain.interactor;

import com.example.rus.petsocialapp.data.datasources.FirebaseDataSource;
import com.example.rus.petsocialapp.presentation.model.FindFriends;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FindFriendsInteractor {
    public FirebaseRecyclerOptions<FindFriends> getOptions(String searchTextInput) {
        return FirebaseDataSource.getFriendsSearchOptions(searchTextInput);
    }
}
