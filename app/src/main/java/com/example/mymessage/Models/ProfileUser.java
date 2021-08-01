package com.example.mymessage.Models;

public class ProfileUser {
    String live, work, gender;

    public ProfileUser() {
    }

    public ProfileUser(String live, String work, String gender) {
        this.live = live;
        this.work = work;
        this.gender = gender;
    }

    public ProfileUser(String live, String work) {
        this.live = live;
        this.work = work;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
