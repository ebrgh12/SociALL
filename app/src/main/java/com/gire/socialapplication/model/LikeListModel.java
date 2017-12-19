package com.gire.socialapplication.model;

/**
 * Created by girish on 7/10/2017.
 */

public class LikeListModel {

    String userName;
    String userImage;

    public LikeListModel(String userName, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
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

}
