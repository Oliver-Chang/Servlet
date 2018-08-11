package com.oliverch.common.enumeration;

public enum Education {
    Other("大专以下"), PostSecEducation("大专"), Undergraduate("本科"),Postgraduate("研究生");

    private String education;

    private Education(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return this.education;
    }
}
