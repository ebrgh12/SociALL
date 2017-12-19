package com.gire.socialapplication.model.groups;

import java.util.List;

/**
 * Created by Manoj on 7/13/2017.
 */

public class GroupsMain {

    String groupId;
    String isAdmin;
    String groupName;
    List<GroupUserList> groupUserLists;

    public GroupsMain(String groupId, String isAdmin, String groupName, List<GroupUserList> groupUserLists) {
        this.groupId = groupId;
        this.isAdmin = isAdmin;
        this.groupName = groupName;
        this.groupUserLists = groupUserLists;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<GroupUserList> getGroupUserLists() {
        return groupUserLists;
    }

    public void setGroupUserLists(List<GroupUserList> groupUserLists) {
        this.groupUserLists = groupUserLists;
    }

}
