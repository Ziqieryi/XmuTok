package com.example.gsy.entity;


public class VideoEntity {
    int videoId; //视频ID
    String playUrl; //视频播放地址
    String coverUrl; //视频封面地址
    int favCount;//视频点赞总数
    int comCount;//视频评论总数
    boolean isFav;//是否点赞
    String title;//视频标题

    public VideoEntity(int videoId, AuthorFragmentData author, String playUrl, String coverUrl, int favCount, int comCount, boolean isFav, String title) {
        this.videoId = videoId;
        this.playUrl = playUrl;
        this.coverUrl = coverUrl;
        this.favCount = favCount;
        this.comCount = comCount;
        this.isFav = isFav;
        this.title = title;
    }
    public VideoEntity(){

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
}
