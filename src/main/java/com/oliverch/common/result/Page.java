package com.oliverch.common.result;

import com.google.gson.Gson;

import java.util.List;

public class Page<T> {
    private Integer page;
    private Integer pages;
    private Integer page_size;
    private Boolean has_next = false;
    private Boolean has_prev = false;
    private Integer total;
    private List<T> data;


    public Page(Integer page, Integer page_size, Integer total, List<T> data) {
        this.page = page;
        this.page_size = page_size;
        this.total = total;
        this.pages = total / page_size + 1;

        if (page < this.pages) {
            this.has_next = true;
        }
        if (page > 1) {
            this.has_prev = true;
        }
        this.data = data;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
