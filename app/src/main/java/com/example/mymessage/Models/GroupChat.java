package com.example.mymessage.Models;

public class GroupChat {

    String createdBy, groupId, groupName, profilePicGroup;
    Long timestamp;
    public GroupChat() {
    }

    public GroupChat(String createdBy, String groupId, String groupName, Long timestamp) {
        this.createdBy = createdBy;
        this.groupId = groupId;
        this.groupName = groupName;
        this.timestamp = timestamp;
    }

    public String getProfilePicGroup() {
        return profilePicGroup;
    }

    public void setProfilePicGroup(String profilePicGroup) {
        this.profilePicGroup = profilePicGroup;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
