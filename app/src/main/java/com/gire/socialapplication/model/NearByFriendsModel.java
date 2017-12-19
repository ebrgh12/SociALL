package com.gire.socialapplication.model;

/**
 * Created by girish on 6/15/2017.
 */

public class NearByFriendsModel {
    String userId;
    String userName;
    String userImage;
    String distance;
    String rating;

    public NearByFriendsModel(String userId, String userName, String userImage, String distance) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.distance = distance;
    }

    public NearByFriendsModel(String userId, String userName, String userImage, String distance, String rating) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.distance = distance;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}