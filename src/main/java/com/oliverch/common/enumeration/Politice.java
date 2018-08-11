package com.oliverch.common.enumeration;

public enum Politice {
    Other("其他"), Menber("团员"), ProbMember("预备党员"), PartyMenber("党员");

    private String politice;

    private Politice(String politice) {
        this.politice = politice;
    }

    public String getPolitice() {
        return politice;
    }

    public void setPolitice(String politice) {
        this.politice = politice;
    }


    @Override
    public String toString() {
        return this.politice;
    }
}
