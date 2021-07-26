package com.example.mymessage.Models;

public class Friends {
    String nameFriend, emailFriend, friendId, profilepic;
    Long msgTimeLast;

    public Friends() {
    }

    public Friends(String nameFriend, String emailFriend, String friendId, String profilepic) {
        this.nameFriend = nameFriend;
        this.emailFriend = emailFriend;
        this.friendId = friendId;
        this.profilepic = profilepic;
    }

    public Friends(String nameFriend, String emailFriend, Long msgTimeLast, String friendId, String profilepic) {
        this.nameFriend = nameFriend;
        this.emailFriend = emailFriend;
        this.msgTimeLast = msgTimeLast;
        this.friendId = friendId;
        this.profilepic = profilepic;
    }

    public Long getMsgTimeLast() {
        return msgTimeLast;
    }

    public void setMsgTimeLast(Long msgTimeLast) {
        this.msgTimeLast = msgTimeLast;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getNameFriend() {
        return nameFriend;
    }

    public void setNameFriend(String nameFriend) {
        this.nameFriend = nameFriend;
    }

    public String getEmailFriend() {
        return emailFriend;
    }

    public void setEmailFriend(String emailFriend) {
        this.emailFriend = emailFriend;
    }
}
