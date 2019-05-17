package com.xiaour.spring.boot.jsonBean.jsfp;

public class JsfpJson {
    /**
     * @Author: huangxiangfei
     * @CreateDate: 2018/10/30$ 17:42$
     */
    private Boolean isok;
    private String msg;
    private JsfpTxaJson data;


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

    public JsfpTxaJson getData() {
        return data;
    }

    public void setData(JsfpTxaJson data) {
        this.data = data;
    }
}
