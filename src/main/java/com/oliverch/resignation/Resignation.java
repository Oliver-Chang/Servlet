package com.oliverch.resignation;

import java.io.Serializable;
import java.sql.Date;

import com.google.gson.Gson;

public class Resignation implements Serializable {
    private String id;
    private String name;
    private String department;
    private String post;
    private String entryTime;
    private String resignationType;
    private String endDate;
    private Integer rId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getResignationType() {
        return resignationType;
    }

    public void setResignationType(String resignationType) {
        this.resignationType = resignationType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}