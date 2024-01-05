package com.example.unilink.Models;

import com.google.firebase.database.ServerValue;

public class PostModel {

    private String postKey;
    private String postTitle;
    private String postDescription;
    private String postImage;
    private String userName;
    private Object timeStamp;

    public PostModel(String postTitle, String postDescription, String postImage,String userName) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.userName  = userName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public PostModel() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
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

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userId) {
        this.userName = userName;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
