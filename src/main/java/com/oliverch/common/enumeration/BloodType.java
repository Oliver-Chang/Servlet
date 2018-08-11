package com.oliverch.common.enumeration;

public enum BloodType {
    Other("其他"), TypeO("O"), TypeA("A"), TypeB("B"), TypeAB("AB");
    private String bloodType;

    private BloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }


    @Override
    public String toString() {
        return this.bloodType;
    }
}
