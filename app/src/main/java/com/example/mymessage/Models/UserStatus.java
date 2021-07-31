package com.example.mymessage.Models;

import java.util.ArrayList;

public class UserStatus {
    private String userId, profileImage;
    private long lastUpdated;
    private ArrayList<Status> statuses;

    public UserStatus() {
    }

    public UserStatus(String userId, String profileImage, long lastUpdated, ArrayList<Status> statuses) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.lastUpdated = lastUpdated;
        this.statuses = statuses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
