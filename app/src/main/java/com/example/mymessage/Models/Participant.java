package com.example.mymessage.Models;

public class Participant {
    String role, userId;
    Long timestamp;

    public Participant() {
    }

    public Participant(String role, String userId, Long timestamp) {
        this.role = role;
        this.userId = userId;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
