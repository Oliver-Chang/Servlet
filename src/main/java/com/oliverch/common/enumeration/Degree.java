package com.oliverch.common.enumeration;

public enum Degree {
    Postdoctoral("博士后"), Doctor("博士"), DoubleDegree("双学士"), Master("硕士"), bachelor("学士"), Other("无学位");

    private String degree;

    private Degree(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return this.degree;
    }
}
