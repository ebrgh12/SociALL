package com.gire.socialapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish on 7/6/2017.
 */

public class HomePagePostModel {
    String postId;
    String postTitle;
    String postDescription;
    String totalLikes;
    String totalComments;
    Integer likeStatus;
    Integer commentStatus;
    List<String> postMediaFile = new ArrayList<String>();

    public HomePagePostModel(String postId, String postTitle, String postDescription, String totalLikes,
                             String totalComments, List<String> postMediaFile) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.postMediaFile = postMediaFile;
    }

    public HomePagePostModel(String postId, String postTitle, String postDescription, String totalLikes,
                             String totalComments, Integer likeStatus, Integer commentStatus,
                             List<String> postMediaFile) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.likeStatus = likeStatus;
        this.commentStatus = commentStatus;
        this.postMediaFile = postMediaFile;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public List<String> getPostMediaFile() {
        return postMediaFile;
    }

    public void setPostMediaFile(List<String> postMediaFile) {
        this.postMediaFile = postMediaFile;
    }

    public Integer getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(Integer likeStatus) {
        this.likeStatus = likeStatus;
    }

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

}