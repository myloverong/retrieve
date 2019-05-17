package com.xiaour.spring.boot.jsonBean.dzfp;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2018-10-25 15:52:1
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class DzfpTxaJson {

    private String fpdm;
    private String fphm;
    private String fplx;
    private String kprq;
    private String fpcc;
    private String cysj;
    private Data data;
    private String err;
    @JsonProperty("data_json")
    private DataJson dataJson;
    public void setFpdm(String fpdm) {
         this.fpdm = fpdm;
     }
     public String getFpdm() {
         return fpdm;
     }

    public void setFphm(String fphm) {
         this.fphm = fphm;
     }
     public String getFphm() {
         return fphm;
     }

    public void setFplx(String fplx) {
         this.fplx = fplx;
     }
     public String getFplx() {
         return fplx;
     }

    public void setKprq(String kprq) {
         this.kprq = kprq;
     }
     public String getKprq() {
         return kprq;
     }

    public void setFpcc(String fpcc) {
         this.fpcc = fpcc;
     }
     public String getFpcc() {
         return fpcc;
     }

    public void setCysj(String cysj) {
         this.cysj = cysj;
     }
     public String getCysj() {
         return cysj;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

    public void setErr(String err) {
         this.err = err;
     }
     public String getErr() {
         return err;
     }

    public void setDataJson(DataJson dataJson) {
         this.dataJson = dataJson;
     }
     public DataJson getDataJson() {
         return dataJson;
     }

}