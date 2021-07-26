package com.example.mymessage.Models;

public class Groups {
    String groupId, groupName ,userIdCreated, profilepic;
    Long timestamp;
    Participants participants;

    public Groups() {
    }

    public Groups(String groupId, String groupName, String userIdCreated, Long timestamp) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userIdCreated = userIdCreated;
        this.timestamp = timestamp;
    }

//    public Groups(String groupId, String groupName, String userIdCreated, Long timestamp, Participants participants) {
//        this.groupId = groupId;
//        this.groupName = groupName;
//        this.userIdCreated = userIdCreated;
//        this.timestamp = timestamp;
//        this.participants = participants;
//    }


    public Groups(String groupId, String groupName, String userIdCreated, String profilepic, Long timestamp) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userIdCreated = userIdCreated;
        this.profilepic = profilepic;
        this.timestamp = timestamp;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
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

    public String getUserIdCreated() {
        return userIdCreated;
    }

    public void setUserIdCreated(String userIdCreated) {
        this.userIdCreated = userIdCreated;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Participants getParticipants() {
        return participants;
    }

    public void setParticipants(Participants participants) {
        this.participants = participants;
    }
}
