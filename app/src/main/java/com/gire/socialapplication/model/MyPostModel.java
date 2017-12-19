package com.gire.socialapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish on 6/29/2017.
 */

public class MyPostModel {
    String title;
    String description;
    List<String> fileList = new ArrayList<>();

    public MyPostModel(String title, String description, List<String> fileList) {
        this.title = title;
        this.description = description;
        this.fileList = fileList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

}
