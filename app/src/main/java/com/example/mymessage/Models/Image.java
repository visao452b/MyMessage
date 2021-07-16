package com.example.mymessage.Models;

public class Image {
    String groupId, profilePicGroup;

    public Image() {
    }

    public Image(String groupId, String profilePicGroup) {
        this.groupId = groupId;
        this.profilePicGroup = profilePicGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProfilePicGroup() {
        return profilePicGroup;
    }

    public void setProfilePicGroup(String profilePicGroup) {
        this.profilePicGroup = profilePicGroup;
    }
}
