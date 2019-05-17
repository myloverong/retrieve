package com.xiaour.spring.boot.jsonBean.dzfp;

public class DzfpJson {
    private Boolean isok;
    private String msg;
    private DzfpTxaJson data;


    public Boolean getIsok() {
        return isok;
    }

    public void setIsok(Boolean isok) {
        this.isok = isok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DzfpTxaJson getData() {
        return data;
    }

    public void setData(DzfpTxaJson data) {
        this.data = data;
    }
}
