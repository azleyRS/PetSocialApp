package com.example.rus.petsocialapp.data.datasources;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rus.petsocialapp.presentation.model.Comments;
import com.example.rus.petsocialapp.presentation.model.FindFriends;
import com.example.rus.petsocialapp.presentation.model.Friends;
import com.example.rus.petsocialapp.presentation.model.Messages;
import com.example.rus.petsocialapp.presentation.model.Posts;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FirebaseDataSource {

    private static final String SUCCESS = "success";

    public static Single<String> login(final String email, final String password) {
        return Single.create(emitter -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    emitter.onSuccess(SUCCESS);
                } else {
                    emitter.onSuccess(task.getException().getMessage());
                }
            });
        });
    }

    public static Single<String> getCurrentUser() {
        return Single.create(emitter -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                emitter.onSuccess(SUCCESS);
            } else {
                emitter.onSuccess("fail");
            }
        });
    }

    public static Single<String> createUser(String email, String password) {
        return Single.create(emitter -> {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    emitter.onSuccess(SUCCESS);
                } else {
                    emitter.onSuccess(task.getException().getMessage());
                }
            });
        });
    }

    public static Single<String> uploadImageToFirebaseStorage(Uri data) {
        return Single.create(emitter -> {
            StorageReference profileImageRef = FirebaseStorage.getInstance()
                    .getReference("profilepics/" + FirebaseAuth.getInstance()
                            .getCurrentUser().getUid() + ".jpg");
            if (data!=null){
                profileImageRef.putFile(data).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))                  ;
                }).addOnFailureListener(e -> emitter.onSuccess("Error occurred: " + e.getMessage()));
            }
        });
    }

    public static Single<String> updateUserAccount(HashMap<String, Object> userMap) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(userMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            emitter.onSuccess(SUCCESS);
                        } else {
                            emitter.onSuccess(task.getException().getMessage());
                        }
                    });
        });
    }

    public static Single<String> checkUserAccountExist() {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        emitter.onSuccess(SUCCESS);
                    } else {
                        emitter.onSuccess("Account is not created yet");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    public static FirebaseRecyclerOptions<Posts> getPostOptions() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Posts")
                .orderByChild("millis")
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(query, Posts.class)
                .build();
    }

    public static Single<String> addPost(Uri imageUri, String description,String saveCurrentDate,String saveCurrentTime,long millis) {
        return Single.create(emitter -> {
            StorageReference filePath =  FirebaseStorage.getInstance().getReference().child("Post Images")
                    .child(imageUri.getLastPathSegment() + saveCurrentDate + saveCurrentTime +".jpg");
            filePath.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(currentUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String userFullname = dataSnapshot.child("displayName").getValue().toString();
                                    String userProfileImage = dataSnapshot.child("photoUrl").getValue().toString();

                                    HashMap<String, Object> postsMap = new HashMap<>();
                                    postsMap.put("uid", currentUserId);
                                    postsMap.put("date", saveCurrentDate);
                                    postsMap.put("time", saveCurrentTime);
                                    postsMap.put("description", description);
                                    postsMap.put("postimage", downloadUrl);
                                    postsMap.put("profileImage", userProfileImage);
                                    postsMap.put("fullname", userFullname);
                                    postsMap.put("millis", millis);

                                    FirebaseDatabase.getInstance().getReference().child("Posts")
                                            .child(currentUserId + saveCurrentDate+saveCurrentTime).updateChildren(postsMap)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()){
                                                    emitter.onSuccess(SUCCESS);
                                                } else {
                                                    emitter.onSuccess(task1.getException().getMessage());
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }).addOnFailureListener(e -> emitter.onSuccess(e.getMessage()));
                }
            });
        });
    }

    public static Single<String> addComment(String comment, String saveCurrentDate, String saveCurrentTime, String postKey) {
        return Single.create(emitter -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String userName = dataSnapshot.child("displayName").getValue().toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("uid", currentUserId);
                        hashMap.put("comment", comment);
                        hashMap.put("date", saveCurrentDate);
                        hashMap.put("time", saveCurrentTime);
                        hashMap.put("username", userName);

                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Comments")
                                .child(currentUserId + saveCurrentDate + saveCurrentTime)
                                .updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            emitter.onSuccess(SUCCESS);
                                        } else {
                                            emitter.onSuccess(task.getException().getMessage());
                                        }
                                    }
                                });
                    } else {
                        emitter.onSuccess("No such data found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    public static FirebaseRecyclerOptions<Comments> getCommentsOptions(String postKey) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Comments")
                .limitToLast(50);

        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(query, Comments.class)
                .build();
        return options;
    }


    public static Single<Map<String,String>> getPost(String postKey) {
        return Single.create(emitter -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("description", dataSnapshot.child("description").getValue().toString());
                            hashMap.put("image", dataSnapshot.child("postimage").getValue().toString());
                            hashMap.put("currentUserId", currentUserId);
                            hashMap.put("databaseUserId", dataSnapshot.child("uid").getValue().toString());
                            emitter.onSuccess(hashMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }

    public static Single<String> editPost(String newDescription, String postKey) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey)
                    .child("description").setValue(newDescription);
            emitter.onSuccess(SUCCESS);
        });
    }

    public static Single<String> deletePost(String postKey, String imageUrl) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).removeValue();
            FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                    .delete().addOnSuccessListener(aVoid -> emitter.onSuccess(SUCCESS));
        });
    }

    public static Single<Map<String,String>> getProfileInfo() {
        return Single.create(emitter -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String myProfileImage = dataSnapshot.child("photoUrl").getValue().toString();
                                String myProfileName = dataSnapshot.child("displayName").getValue().toString();
                                String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                                String myProfileDOB = dataSnapshot.child("dob").getValue().toString();
                                String myProfileFavDrinks = dataSnapshot.child("drinks").getValue().toString();
                                String myProfileGender = dataSnapshot.child("gender").getValue().toString();
                                String myProfileCity = dataSnapshot.child("city").getValue().toString();
                                String myProfileCountry = dataSnapshot.child("country").getValue().toString();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("photoUrl", myProfileImage);
                                hashMap.put("displayName", myProfileName);
                                hashMap.put("status", myProfileStatus);
                                hashMap.put("dob", myProfileDOB);
                                hashMap.put("drinks", myProfileFavDrinks);
                                hashMap.put("gender", myProfileGender);
                                hashMap.put("city", myProfileCity);
                                hashMap.put("country", myProfileCountry);

                                emitter.onSuccess(hashMap);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }

    public static FirebaseRecyclerOptions<FindFriends> getFriendsSearchOptions(String searchTextInput) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .orderByChild("displayName")
                .startAt(searchTextInput)
                .endAt(searchTextInput + "\uf8ff")
                .limitToLast(50);

        FirebaseRecyclerOptions<FindFriends> options = new FirebaseRecyclerOptions.Builder<FindFriends>()
                .setQuery(query, FindFriends.class)
                .build();
        return options;
    }

    public static Single<Map<String,String>> getProfileInfo(String recieverUserId) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference().child("Users").child(recieverUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String myProfileImage = dataSnapshot.child("photoUrl").getValue().toString();
                                String myProfileName = dataSnapshot.child("displayName").getValue().toString();
                                String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                                String myProfileDOB = dataSnapshot.child("dob").getValue().toString();
                                String myProfileFavDrinks = dataSnapshot.child("drinks").getValue().toString();
                                String myProfileGender = dataSnapshot.child("gender").getValue().toString();
                                String myProfileCity = dataSnapshot.child("city").getValue().toString();
                                String myProfileCountry = dataSnapshot.child("country").getValue().toString();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("photoUrl", myProfileImage);
                                hashMap.put("displayName", myProfileName);
                                hashMap.put("status", myProfileStatus);
                                hashMap.put("dob", myProfileDOB);
                                hashMap.put("drinks", myProfileFavDrinks);
                                hashMap.put("gender", myProfileGender);
                                hashMap.put("city", myProfileCity);
                                hashMap.put("country", myProfileCountry);

                                emitter.onSuccess(hashMap);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }

    public static Single<String> checFriendshipStatus(String recieverUserId) {
        return Single.create(emitter -> {
            String senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("FriendRequests").child(senderUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(recieverUserId)){
                                String requestType = dataSnapshot.child(recieverUserId).child("request_type").getValue().toString();
                                emitter.onSuccess(requestType);
                            } else {
                                //here
                                FirebaseDatabase.getInstance().getReference().child("Friends").child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(recieverUserId)){
                                            emitter.onSuccess("friends");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }

    public static Single<String> cancelFriendRequest(String recieverUserId) {
        return Single.create(emitter -> {
            String senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("FriendRequests")
                    .child(senderUserId)
                    .child(recieverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference().child("FriendRequests")
                                .child(recieverUserId)
                                .child(senderUserId)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    emitter.onSuccess("not friends");
                                }
                            }
                        });
                    }
                }
            });
        });
    }

    public static Single<String> getCurrentUserUid() {
        return Single.create(emitter -> {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            emitter.onSuccess(currentUser);
        });
    }

    public static Single<String> sendFriendRequest(String recieverUserId) {
        return Single.create(emitter -> {
            DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
            String senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            friendRequestRef.child(senderUserId).child(recieverUserId).child("request_type").setValue("sent")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            friendRequestRef.child(recieverUserId).child(senderUserId).child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                emitter.onSuccess("request sent");
                                            }
                                        }
                                    });
                        }
                    });
        });
    }

    public static Single<String> acceptFriendRequest(String recieverUserId, String saveCurrentDate) {
        return Single.create(emitter -> {
            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
            String senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");

            friendsRef.child(senderUserId).child(recieverUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friendsRef.child(recieverUserId).child(senderUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    friendRequestRef.child(senderUserId).child(recieverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                friendRequestRef.child(recieverUserId).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            emitter.onSuccess("friends");
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });

        });
    }

    public static Single<String> unfriend(String recieverUserId) {
        return Single.create(emitter -> {
            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
            String senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            friendsRef.child(senderUserId).child(recieverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friendsRef.child(recieverUserId).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    emitter.onSuccess("not friends");
                                }
                            }
                        });
                    }
                }
            });
        });
    }

    public static FirebaseRecyclerOptions<Friends> getAllFriendsOptions() {
        String online_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Friends").child(online_user_id)
                .limitToLast(50);

        FirebaseRecyclerOptions<Friends> options = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(query, Friends.class)
                .build();
        return options;
    }

    public static Single<String> getProfileImage(String accountUserId) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference().child("Users").child(accountUserId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String profileImage = dataSnapshot.child("photoUrl").getValue().toString();
                                emitter.onSuccess(profileImage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }

    public static Single<String> sendMessage(String messageText, String accountUserId, String saveCurrentDate, String saveCurrentTime) {
        return Single.create(emitter -> {
            String messageSenderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String messageSenderRef = "Messages/" + messageSenderId + '/' + accountUserId;
            String messageREceiverRef = "Messages/" + accountUserId + '/' + messageSenderId;

            DatabaseReference userMessageRef = FirebaseDatabase.getInstance().getReference()
                    .child("Messages").child(messageSenderId).child(accountUserId).push();
            String messagePushId = userMessageRef.getKey();

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", messageText);
            hashMap.put("time", saveCurrentTime);
            hashMap.put("date", saveCurrentDate);
            hashMap.put("type", "text");
            hashMap.put("from", messageSenderId);

            Map<String, Object> messageBodyDetails = new HashMap<>();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushId, hashMap);
            messageBodyDetails.put(messageREceiverRef + "/" + messagePushId, hashMap);

            FirebaseDatabase.getInstance().getReference().updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        emitter.onSuccess(SUCCESS);
                    } else {
                        emitter.onSuccess(task.getException().getMessage());
                    }
                }
            });
        });
    }

    public static Observable<Messages> fetchMessages(String accountUserId) {
        return Observable.create(emitter -> {
            String messageSenderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Messages").child(messageSenderId).child(accountUserId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()){
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        emitter.onNext(messages);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }
}
