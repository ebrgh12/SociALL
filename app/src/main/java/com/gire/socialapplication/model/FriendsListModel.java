package com.gire.socialapplication.model;

/**
 * Created by girish on 7/8/2017.
 */

public class FriendsListModel {

    String friendId;
    String friendName;
    String userImage;
    String modeId;
    Integer actionAvailable;

    public FriendsListModel(String friendId, String friendName, String userImage,
                            String modeId, Integer actionAvailable) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.userImage = userImage;
        this.modeId = modeId;
        this.actionAvailable = actionAvailable;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public Integer getActionAvailable() {
        return actionAvailable;
    }

    public void setActionAvailable(Integer actionAvailable) {
        this.actionAvailable = actionAvailable;
    }

}