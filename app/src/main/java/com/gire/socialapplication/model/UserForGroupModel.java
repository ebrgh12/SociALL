package com.gire.socialapplication.model;

/**
 * Created by girish on 7/10/2017.
 */

public class UserForGroupModel {

    String friendId;
    String userName;
    String userImage;
    Integer isSelected;

    public UserForGroupModel(String friendId, String userName, String userImage, Integer isSelected) {
        this.friendId =friendId;
        this.userName = userName;
        this.userImage = userImage;
        this.isSelected = isSelected;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
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

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

}
