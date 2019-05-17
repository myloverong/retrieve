package com.xiaour.spring.boot.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DzfpInvoiceQd {
    private String id;

    private String fpdm;

    private String fphm;

    private String wpMc;

    private String wpXh;

    private String wpDw;

    private BigDecimal sl;

    private BigDecimal dj;

    private BigDecimal je;

    private BigDecimal slv;

    private BigDecimal se;


    private String xfNsrsbh;


    private String lslbz;

    private String yhzc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFpdm() {
        return fpdm;
    }

    public void setFpdm(String fpdm) {
        this.fpdm = fpdm == null ? null : fpdm.trim();
    }

    public String getFphm() {
        return fphm;
    }

    public void setFphm(String fphm) {
        this.fphm = fphm == null ? null : fphm.trim();
    }

    public String getWpMc() {
        return wpMc;
    }

    public void setWpMc(String wpMc) {
        this.wpMc = wpMc == null ? null : wpMc.trim();
    }

    public String getWpXh() {
        return wpXh;
    }

    public void setWpXh(String wpXh) {
        this.wpXh = wpXh == null ? null : wpXh.trim();
    }

    public String getWpDw() {
        return wpDw;
    }

    public void setWpDw(String wpDw) {
        this.wpDw = wpDw == null ? null : wpDw.trim();
    }

    public BigDecimal getSl() {
        return sl;
    }

    public void setSl(BigDecimal sl) {
        this.sl = sl;
    }

    public BigDecimal getDj() {
        return dj;
    }

    public void setDj(BigDecimal dj) {
        this.dj = dj;
    }

    public BigDecimal getJe() {
        return je;
    }

    public void setJe(BigDecimal je) {
        this.je = je;
    }

    public BigDecimal getSlv() {
        return slv;
    }

    public void setSlv(BigDecimal slv) {
        this.slv = slv;
    }

    public BigDecimal getSe() {
        return se;
    }

    public void setSe(BigDecimal se) {
        this.se = se;
    }


    public String getXfNsrsbh() {
        return xfNsrsbh;
    }

    public void setXfNsrsbh(String xfNsrsbh) {
        this.xfNsrsbh = xfNsrsbh == null ? null : xfNsrsbh.trim();
    }


    public String getLslbz() {
        return lslbz;
    }

    public void setLslbz(String lslbz) {
        this.lslbz = lslbz == null ? null : lslbz.trim();
    }

    public String getYhzc() {
        return yhzc;
    }

    public void setYhzc(String yhzc) {
        this.yhzc = yhzc == null ? null : yhzc.trim();
    }
}