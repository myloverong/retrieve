package com.xiaour.spring.boot.jsonBean.ptfp;

public class PtfpJson {
    /**
     * @Author: huangxiangfei
     * @CreateDate: 2018/10/30$ 17:58$
     */
    private Boolean isok;
    private String msg;
    private PtfpTaxJson data;


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

    public PtfpTaxJson getData() {
        return data;
    }

    public void setData(PtfpTaxJson data) {
        this.data = data;
    }
}
