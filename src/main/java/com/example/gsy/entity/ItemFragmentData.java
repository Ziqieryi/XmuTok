package com.example.gsy.entity;


import java.io.Serializable;

public class ItemFragmentData implements Serializable {
    int videoId; //视频ID
    String playUrl; //视频播放地址
    String coverUrl; //视频封面地址
    int favCount;//视频点赞总数
    int comCount;//视频评论总数
    boolean isFav;//是否点赞
    String title;//视频标题
    int userId;//用户ID
    String userName;//用户账号(用户昵称)
    int followCount;//关注数
    int followerCount;//粉丝数
    boolean isFollow;//是否关注
    String signature;//个性签名
    String avatar;//头像地址
    String backGroundImage;//背景图地址

    public ItemFragmentData() {
    }

    public ItemFragmentData(int videoId, String playUrl, String coverUrl, int favCount, int comCount, boolean isFav, String title, int userId, String userName, int followCount, int followerCount, boolean isFollow, String signature, String avatar, String backGroundImage) {
        this.videoId = videoId;
        this.playUrl = playUrl;
        this.coverUrl = coverUrl;
        this.favCount = favCount;
        this.comCount = comCount;
        this.isFav = isFav;
        this.title = title;
        this.userId = userId;
        this.userName = userName;
        this.followCount = followCount;
        this.followerCount = followerCount;
        this.isFollow = isFollow;
        this.signature = signature;
        this.avatar = avatar;
        this.backGroundImage = backGroundImage;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getComCount() {
        return comCount;
    }

    public void setComCount(int comCount) {
        this.comCount = comCount;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
