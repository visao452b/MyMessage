package com.example.mymessage.Models;

public class UserGroup {
    String userId, groupId;

    public UserGroup() {
    }

    public UserGroup(String userId, String groupUserId) {
        this.userId = userId;
        this.groupId = groupUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupUserId) {
        this.groupId = groupUserId;
    }
}
