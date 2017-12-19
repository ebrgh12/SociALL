package com.gire.socialapplication.model.groups;

/**
 * Created by Manoj on 7/13/2017.
 */

public class GroupUserList {

    String userId;
    String userName;
    String userProfile;
    Boolean flag;

    public GroupUserList(String userId, String userName, String userProfile) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
    }

    public GroupUserList(String userId, String userName, String userProfile, Boolean flag) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.flag = flag;
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

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
