package com.oliverch.post;

import com.google.gson.Gson;

import java.io.Serializable;

public class Post implements Serializable {
    private Integer rId;
    private String postId;
    private String postName;
    private String postType;
    //private Integer maxNumber;

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }


    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

//    public Integer getMaxNumber() {
//        return maxNumber;
//    }
//
//    public void setMaxNumber(Integer maxNumber) {
//        this.maxNumber = maxNumber;
//    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}