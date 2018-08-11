package com.oliverch.common.enumeration;

public enum WorkType {
    TempWork("临时员工"), RegularStaff("正式员工");
    private String workType;

    WorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    @Override
    public String toString() {
        return this.workType;
    }
}
