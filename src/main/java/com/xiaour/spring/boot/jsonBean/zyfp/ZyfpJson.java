package com.xiaour.spring.boot.jsonBean.zyfp;

public class ZyfpJson {
    /**
     * @Author: huangxiangfei
     * @CreateDate: 2018/10/30$ 17:53$
     */
    private Boolean isok;
    private String msg;
    private ZyfpTxaJson data;


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

    public ZyfpTxaJson getData() {
        return data;
    }

    public void setData(ZyfpTxaJson data) {
        this.data = data;
    }
}
