package com.example.lab7.Model;

public class Page<T>{
    private T data;
    private int currenPage, totalPage;

    public Page() {
    }

    public Page(T data, int currenPage, int totalPage) {
        this.data = data;
        this.currenPage = currenPage;
        this.totalPage = totalPage;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCurrenPage() {
        return currenPage;
    }

    public void setCurrenPage(int currenPage) {
        this.currenPage = currenPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

