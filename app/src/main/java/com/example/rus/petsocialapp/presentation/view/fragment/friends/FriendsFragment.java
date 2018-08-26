package com.example.rus.petsocialapp.presentation.view.fragment.friends;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.FriendsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.FriendsPresenter;
import com.example.rus.petsocialapp.presentation.view.FriendsView;
import com.example.rus.petsocialapp.presentation.view.adapter.AllFriendsAdapter;

public class FriendsFragment extends Fragment implements FriendsView {

    //before dagger2
    FriendsPresenter friendsPresenter = new FriendsPresenter(new FriendsInteractor());

    private RecyclerView recyclerView;
    private AllFriendsAdapter allFriendsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendsPresenter.attachView(this);

        initViews(view);
        displayFriends();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        friendsPresenter.detachView();
    }

    private void displayFriends() {
        allFriendsAdapter = new AllFriendsAdapter(friendsPresenter.getOptions(),getActivity());
        recyclerView.setAdapter(allFriendsAdapter);
        allFriendsAdapter.startListening();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.friends_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static Fragment newInstance() {
        return new FriendsFragment();
    }
}
