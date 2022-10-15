package com.example.gsy.entity;

import java.io.Serializable;

public class AuthorFragmentData implements Serializable {
    int userId;//用户ID
    String userName;//用户账号(用户昵称)
    int followCount;//关注数
    int followerCount;//粉丝数
    boolean isFollow;//是否关注
    String signature;//个性签名
    String avatar;//头像地址
    String backGroundImage;//背景图地址

    public AuthorFragmentData(int userId, String userName, int followCount, int followerCount, boolean isFollow, String signature, String avatar, String backGroundImage) {
        this.userId = userId;
        this.userName = userName;
        this.followCount = followCount;
        this.followerCount = followerCount;
        this.isFollow = isFollow;
        this.signature = signature;
        this.avatar = avatar;
        this.backGroundImage = backGroundImage;
    }
    public AuthorFragmentData(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackGroundImage() {
        return backGroundImage;
    }

    public void setBackGroundImage(String backGroundImage) {
        this.backGroundImage = backGroundImage;
    }
}
