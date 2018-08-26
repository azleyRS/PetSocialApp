package com.example.rus.petsocialapp.presentation.view.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rus.petsocialapp.R;
import com.example.rus.petsocialapp.domain.interactor.ChatInteractor;
import com.example.rus.petsocialapp.presentation.model.Messages;
import com.example.rus.petsocialapp.presentation.presenter.ChatPresenter;
import com.example.rus.petsocialapp.presentation.view.ChatView;
import com.example.rus.petsocialapp.presentation.view.adapter.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatView {

    //before dagger2
    ChatPresenter chatPresenter = new ChatPresenter(new ChatInteractor());

    private Toolbar mToolBar;
    private ImageButton sendImageButton, sendMessageNutton;
    private EditText inputText;
    private RecyclerView recyclerView;
    TextView receiverName;
    CircleImageView receiverProfileImage;
    private MessagesAdapter messagesAdapter;
    private List<Messages> messagesList = new ArrayList<>();



    private String accountUserId, userName;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatPresenter.attachView(this);

        accountUserId = getIntent().getStringExtra("accountUserId");
        userName = getIntent().getStringExtra("userName");

        initViews();

        chatPresenter.displayReceiverInfo(accountUserId);

        setOnClickListeners();

        chatPresenter.fetchMessages(accountUserId);

    }

    private void setOnClickListeners() {
        sendMessageNutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatPresenter.sendMessage(inputText.getText().toString(), accountUserId);
            }
        });

    }

    private void initViews() {
        mToolBar = findViewById(R.id.chat_app_bat_layout);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(actionBarView);

        sendMessageNutton = findViewById(R.id.chat_send_message_button);
        sendImageButton = findViewById(R.id.chat_send_image_button);
        inputText = findViewById(R.id.chat_input_message);

        receiverName = findViewById(R.id.custom_profile_name);
        receiverProfileImage = findViewById(R.id.custom_bar_profile_picture);

        messagesAdapter = new MessagesAdapter(messagesList);
        recyclerView = findViewById(R.id.chat_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messagesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatPresenter.detachView();
    }

    public static Intent newIntent(Context context){
        return new Intent(context, ChatActivity.class);
    }

    @Override
    public void loadImage(String s) {
        Glide.with(ChatActivity.this).load(s).into(receiverProfileImage);
    }

    @Override
    public void MessageIsEmpty() {
        inputText.setError("Message is required");
        inputText.requestFocus();
    }

    @Override
    public void success() {
        Toast.makeText(ChatActivity.this, "Message send successfully", Toast.LENGTH_SHORT).show();
        inputText.setText("");
    }

    @Override
    public void fail(String s) {
        Toast.makeText(ChatActivity.this, "Error occured: " + s, Toast.LENGTH_SHORT).show();
        inputText.setText("");
    }

    @Override
    public void addMessage(Messages s) {
        messagesList.add(s);
        messagesAdapter.notifyDataSetChanged();
    }
}
