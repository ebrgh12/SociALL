package com.gire.socialapplication.model;

import android.graphics.drawable.Drawable;

/**
 * Created by girish on 6/20/2017.
 */

public class SentRequestModel {

    String userImage;
    String userName;
    String userId;
    Drawable testDrawable;

    public SentRequestModel(String userImage, String userName, String userId, Drawable testDrawable) {
        this.userImage = userImage;
        this.userName = userName;
        this.userId = userId;
        this.testDrawable = testDrawable;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Drawable getTestDrawable() {
        return testDrawable;
    }

    public void setTestDrawable(Drawable testDrawable) {
        this.testDrawable = testDrawable;
    }

}
