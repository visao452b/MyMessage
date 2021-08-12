package com.example.mymessage.Models;

public class Posts {
    String userPost, contentPost, userIdPost, postId;
    Long timePost;
    int feeling;

    public Posts() {
    }

    public Posts(String userPost, String contentPost, String userIdPost, String postId, Long timePost, int feeling) {
        this.userPost = userPost;
        this.contentPost = contentPost;
        this.userIdPost = userIdPost;
        this.postId = postId;
        this.timePost = timePost;
        this.feeling = feeling;
    }

    public Posts(String userPost, String contentPost, String userIdPost, Long timePost) {
        this.userPost = userPost;
        this.contentPost = contentPost;
        this.userIdPost = userIdPost;
        this.timePost = timePost;
    }

    public Posts(String userPost, String contentPost, String userIdPost, Long timePost, int feeling) {
        this.userPost = userPost;
        this.contentPost = contentPost;
        this.userIdPost = userIdPost;
        this.timePost = timePost;
        this.feeling = feeling;
    }

    public Posts(String userPost, String contentPost, Long timePost, int feeling) {
        this.userPost = userPost;
        this.contentPost = contentPost;
        this.timePost = timePost;
    }

    public String getUserIdPost() {
        return userIdPost;
    }

    public void setUserIdPost(String userIdPost) {
        this.userIdPost = userIdPost;
    }

    public String getUserPost() {
        return userPost;
    }

    public void setUserPost(String userPost) {
        this.userPost = userPost;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }

    public Long getTimePost() {
        return timePost;
    }

    public void setTimePost(Long timePost) {
        this.timePost = timePost;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
