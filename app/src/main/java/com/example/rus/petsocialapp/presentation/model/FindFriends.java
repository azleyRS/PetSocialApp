package com.example.rus.petsocialapp.presentation.model;

public class FindFriends {
    public String photoUrl,displayName,status;

    public FindFriends() {
    }

    public FindFriends(String photoUrl, String displayName, String status) {
        this.photoUrl = photoUrl;
        this.displayName = displayName;
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
