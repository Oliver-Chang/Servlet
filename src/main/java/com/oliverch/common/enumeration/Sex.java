package com.oliverch.common.enumeration;

public enum Sex {
    Man("男"), Woman("女");

    private String sex;
    private Sex(String sex){
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return this.sex;
    }
}
