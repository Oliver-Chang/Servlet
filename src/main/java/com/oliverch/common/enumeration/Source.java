package com.oliverch.common.enumeration;

public enum Source {
    Other("其他"), CampusRecruitment("校园招聘"), SocialRecruitment("社会招聘");
    private String source;

    private Source(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return this.source;
    }
}
