package com.example.mymessage.Models;

public class UserGroup {
    String userId, groupUserId;

    public UserGroup() {
    }

    public UserGroup(String userId, String groupUserId) {
        this.userId = userId;
        this.groupUserId = groupUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }
}
