package com.example.unilink.Models;

import com.google.firebase.database.ServerValue;

public class CommentModel {
    private String content;
    private String uid;
    private String uname;
    private Object timestamp;

    public CommentModel(String content, String uid, String uname) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public CommentModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
