package com.example.mymessage.Models;

public class Participants {
    String role, userId, userName;
    Long timestamp;

    public Participants() {
    }

    public Participants(String role, String userId, String userName, Long timestamp) {
        this.role = role;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
