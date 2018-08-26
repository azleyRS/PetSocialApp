package com.example.rus.petsocialapp.presentation.view.fragment.post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.PostsInteractor;
import com.example.rus.petsocialapp.presentation.presenter.PostsPresenter;
import com.example.rus.petsocialapp.presentation.view.PostsView;
import com.example.rus.petsocialapp.presentation.view.activity.post.PostActivity;
import com.example.rus.petsocialapp.presentation.view.adapter.PostsAdapter;

public class PostsFragment extends Fragment implements PostsView {

    private FloatingActionButton addPostButton;
    private RecyclerView recyclerView;

    //before Dagger2
    PostsPresenter postsPresenter = new PostsPresenter(new PostsInteractor());
    private PostsAdapter postAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnClickListeners();
        postsPresenter.attachView(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postsPresenter.detachView();
    }

    private void setOnClickListeners() {
        addPostButton.setOnClickListener(v -> {
            postsPresenter.addPost();
        });
    }

    private void initViews(View view) {
        addPostButton = view.findViewById(R.id.fragment_posts_floating_button);
        recyclerView = view.findViewById(R.id.all_users_post_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        displayAllUsersPosts();
    }

    private void displayAllUsersPosts() {
        postAdaper = new PostsAdapter(postsPresenter.getOptions(), getContext());
        recyclerView.setAdapter(postAdaper);
        postAdaper.startListening();
    }

    public static Fragment newInstance() {
        return new PostsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdaper.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        postAdaper.stopListening();
    }

    @Override
    public void addPost() {
        startActivity(PostActivity.newIntent(getActivity()));
    }
}
