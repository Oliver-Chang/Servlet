package com.oliverch.common.enumeration;

public enum Marriage {
    Other("其他"), Divorce("离婚"), Widowed("丧偶"), Married("已婚"), Unmarried("未婚");

    private String marriage;

    private Marriage(String marriage) {
        this.marriage = marriage;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    @Override
    public String toString() {
        return this.marriage;
    }
}
