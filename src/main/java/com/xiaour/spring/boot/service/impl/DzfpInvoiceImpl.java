package com.xiaour.spring.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xiaour.spring.boot.core.Result;
import com.xiaour.spring.boot.core.ResultCode;
import com.xiaour.spring.boot.entity.DzfpInvoice;
import com.xiaour.spring.boot.entity.DzfpInvoiceQd;
import com.xiaour.spring.boot.jsonBean.dzfp.DzfpJson;
import com.xiaour.spring.boot.jsonBean.dzfp.DzfpTxaJson;
import com.xiaour.spring.boot.jsonBean.dzfp.Mx;
import com.xiaour.spring.boot.jsonBean.jsfp.JsfpJson;
import com.xiaour.spring.boot.jsonBean.jsfp.JsfpTxaJson;
import com.xiaour.spring.boot.jsonBean.ptfp.PtfpJson;
import com.xiaour.spring.boot.jsonBean.ptfp.PtfpTaxJson;
import com.xiaour.spring.boot.jsonBean.zyfp.ZyfpJson;
import com.xiaour.spring.boot.jsonBean.zyfp.ZyfpTxaJson;
import com.xiaour.spring.boot.mapper.DzfpInvoiceMapper;
import com.xiaour.spring.boot.mapper.DzfpInvoiceQdMapper;
import com.xiaour.spring.boot.service.DzfpInvoiceService;
import com.xiaour.spring.boot.utils.DateTimeUtil;
import com.xiaour.spring.boot.utils.HttpClientUtil;
import com.xiaour.spring.boot.utils.ReadConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class DzfpInvoiceImpl implements DzfpInvoiceService {

    private static final ReadConfigFile readconfig = new ReadConfigFile();
    private static final Gson gson = new Gson();

    @Autowired
    private DzfpInvoiceMapper dzfpInvoiceMapper;
    @Autowired
    private DzfpInvoiceQdMapper dzfpInvoiceQdMapper;

    @Transactional
    @Override
    public Result<Map<String, Object>> findfp(DzfpInvoice dzfpInvoice, String kprq, String kjje, String type) {

        Result<Map<String, Object>> result = new Result<>();
        if (type.equals("10") || type.equals("04") || type.equals("11")) {
            String reg = "^\\d+$";
            if (kjje.length() != 6 || !kjje.matches(reg)) {
                result.setCode(ResultCode.NO_YANZHENG);
                result.setMessage("校验码错误");
                return result;
            }
        } else {
            if (!kjje.contains(".")) {
                result.setCode(ResultCode.NO_YANZHENG);
                result.setMessage("校验码错误");
                return result;
            }
        }
        Map<String, Object> hashMap = new HashMap<>();
        List<DzfpInvoiceQd> listMx = null;
        String resultJson = "";
        DzfpInvoice dzfpInvoice1 = null;
        dzfpInvoice1 = dzfpInvoiceMapper.selectEntityByFpdmAndFphm(dzfpInvoice);
        DzfpJson dzfpJson = null;
        PtfpJson ptfpJson = null;
        ZyfpJson zyfpJson = null;
        JsfpJson jsfpJson = null;
        if (dzfpInvoice1 != null) {
            if (!dzfpInvoice1.getFplx().equals(type)) {
                result.setCode(ResultCode.NO_FPZL);
                result.setMessage("发票种类不正确，请确认");
                result.setData(hashMap);
                return result;
            }
            listMx = dzfpInvoiceQdMapper.selectEntityByFpdmAndFphm(dzfpInvoice1.getFpdm(), dzfpInvoice1.getFphm());
            hashMap.put("invoice", dzfpInvoice1);
            hashMap.put("invoice_mx", listMx);
        } else {
            resultJson = HttpClientUtil.doGet(readconfig.readConfigProperties("config.fpcyUrl"), "fpdm=" + dzfpInvoice.getFpdm() + "&fphm=" + dzfpInvoice.getFphm() + "&kprq=" + kprq + "&kjje=" + kjje);
            if (resultJson == null || resultJson.equals("")) {
                result.setCode(ResultCode.NO_ACCESS);
                result.setMessage("网络繁忙，请稍后再试");
                result.setData(hashMap);
                return result;
            }
            if (Integer.parseInt(type) == 01) {//增值税专用发票
                try {
                    zyfpJson = JSON.parseObject(resultJson, ZyfpJson.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Boolean isok = zyfpJson.getIsok();
                if (isok == false) {
                    result.setCode(ResultCode.NO_RETURN);
                    String msg = zyfpJson.getMsg();
                    if (msg.contains("null")) {
                        result.setMessage("请核对查验数据是否正确");
                    } else {
                        result.setMessage(zyfpJson.getMsg());
                    }
                    return result;
                } else {
                    ZyfpTxaJson zyfpTxaJson = zyfpJson.getData();
                    if (zyfpTxaJson.getDataJson() == null) {
                        result.setCode(ResultCode.NO_ACCESS);
                        result.setMessage("网络繁忙，请稍后再试");
                        result.setData(hashMap);
                        return result;
                    }
                    if (!zyfpTxaJson.getFplx().equals(type)) {
                        result.setCode(ResultCode.NO_FPZL);
                        result.setMessage("发票种类不正确，请确认");
                        result.setData(hashMap);
                        return result;
                    }
                    try {
                        dzfpInvoice.setKprq(DateTimeUtil.getDateTimeTODate(zyfpTxaJson.getDataJson().getKprq()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dzfpInvoice.setFpcc(zyfpTxaJson.getFpcc() != null ? zyfpTxaJson.getFpcc() : "");
                    dzfpInvoice.setJqbh(zyfpTxaJson.getDataJson().getJqbh() != null ? zyfpTxaJson.getDataJson().getJqbh() : "");
                    dzfpInvoice.setHjje(new BigDecimal((zyfpTxaJson.getDataJson().getJe())));
                    dzfpInvoice.setJshjxx(new BigDecimal((zyfpTxaJson.getDataJson().getJshj())));
//              dzfpInvoice.setSlv();
                    dzfpInvoice.setFpJym(zyfpTxaJson.getDataJson().getJym());
                    dzfpInvoice.setHjse(new BigDecimal(zyfpTxaJson.getDataJson().getSe()));
                    dzfpInvoice.setGfsh(zyfpTxaJson.getDataJson().getGfsbh() != null ? zyfpTxaJson.getDataJson().getGfsbh() : "");
                    dzfpInvoice.setGfmc(zyfpTxaJson.getDataJson().getGfmc() != null ? zyfpTxaJson.getDataJson().getGfmc() : "");
                    dzfpInvoice.setGfdzDh(zyfpTxaJson.getDataJson().getGfdzdh() != null ? zyfpTxaJson.getDataJson().getGfdzdh() : "");
                    dzfpInvoice.setGfyhmcYhzh(zyfpTxaJson.getDataJson().getGfyhzh() != null ? zyfpTxaJson.getDataJson().getGfyhzh() : "");
                    dzfpInvoice.setXfsh(zyfpTxaJson.getDataJson().getXfsbh() != null ? zyfpTxaJson.getDataJson().getXfsbh() : "");
                    dzfpInvoice.setXfmc(zyfpTxaJson.getDataJson().getXfmc() != null ? zyfpTxaJson.getDataJson().getXfmc() : "");
                    dzfpInvoice.setXfdzDh(zyfpTxaJson.getDataJson().getXfdzdh() != null ? zyfpTxaJson.getDataJson().getXfdzdh() : "");
                    dzfpInvoice.setXfyhmcYhzh(zyfpTxaJson.getDataJson().getXfyhzh() != null ? zyfpTxaJson.getDataJson().getXfyhzh() : "");
                    dzfpInvoice.setZfbz(zyfpTxaJson.getDataJson().getZfbz() != null ? zyfpTxaJson.getDataJson().getZfbz() : "");
                    dzfpInvoice.setBz(zyfpTxaJson.getDataJson().getBz() != null ? zyfpTxaJson.getDataJson().getBz() : "");
                    dzfpInvoice.setFphm(zyfpTxaJson.getFphm());
                    dzfpInvoice.setFpdm(zyfpTxaJson.getFpdm());
                    dzfpInvoice.setFplx(type);
                    List<com.xiaour.spring.boot.jsonBean.zyfp.Mx> list = zyfpTxaJson.getDataJson().getMx();
                    dzfpInvoiceMapper.insertSelective(dzfpInvoice);//新增发票主体信息
                    List<DzfpInvoiceQd> dzfpInvoiceQdList = new ArrayList<>();
                    if (list.size() != 0) {
                        for (com.xiaour.spring.boot.jsonBean.zyfp.Mx mx : list) {
                            DzfpInvoiceQd dzfpInvoiceQd = new DzfpInvoiceQd();
                            dzfpInvoiceQd.setFpdm(dzfpInvoice.getFpdm());
                            dzfpInvoiceQd.setFphm(dzfpInvoice.getFphm());
                            dzfpInvoiceQd.setWpMc(mx.getMc());
                            dzfpInvoiceQd.setWpXh(mx.getGgxh());
                            dzfpInvoiceQd.setWpDw(mx.getDw());
                            String sl = mx.getSl();
                            if (sl.equals(" ") || sl.equals("")) {
                                sl = "1";
                            }
                            String dj = mx.getDj();
                            if (dj.equals(" ") || dj.equals("")) {
                                dj = mx.getJe();
                            }
                            dzfpInvoiceQd.setSl(new BigDecimal(sl));
                            dzfpInvoiceQd.setDj(new BigDecimal(dj).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                            dzfpInvoiceQd.setJe(new BigDecimal(mx.getJe()));
                            dzfpInvoiceQd.setSlv(new BigDecimal(mx.getSlv()));
                            dzfpInvoiceQd.setSe(new BigDecimal(mx.getSe()));
                            dzfpInvoiceQd.setXfNsrsbh(dzfpInvoice.getXfsh());
                            dzfpInvoiceQdList.add(dzfpInvoiceQd);
                            dzfpInvoiceQdMapper.insertSelective(dzfpInvoiceQd);//新增明细

                        }

                    }
                    hashMap.put("invoice", dzfpInvoice);
                    hashMap.put("invoice_mx", dzfpInvoiceQdList);
                }

            } else if (Integer.parseInt(type) == 02) {//货物运输业增值税专用发票

            } else if (Integer.parseInt(type) == 03) {//机动车销售统一发票

            } else if (Integer.parseInt(type) == 04) {//增值税普通发票
                try {
                    ptfpJson = JSON.parseObject(resultJson, PtfpJson.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Boolean isok = ptfpJson.getIsok();
                if (isok == false) {
                    result.setCode(ResultCode.NO_RETURN);
                    String msg = ptfpJson.getMsg();
                    if (msg.contains("null")) {
                        result.setMessage("请核对查验数据是否正确");
                    } else {
                        result.setMessage(ptfpJson.getMsg());
                    }
                    return result;
                } else {
                    PtfpTaxJson ptfpTaxJson = ptfpJson.getData();
                    if (ptfpTaxJson.getDataJson() == null) {
                        result.setCode(ResultCode.NO_ACCESS);
                        result.setMessage("网络繁忙，请稍后再试");
                        result.setData(hashMap);
                        return result;
                    }
                    if (!ptfpTaxJson.getFplx().equals(type)) {
                        result.setCode(ResultCode.NO_FPZL);
                        result.setMessage("发票种类不正确，请确认");
                        result.setData(hashMap);
                        return result;
                    }
                    dzfpInvoice.setFpcc(ptfpTaxJson.getFpcc());
                    dzfpInvoice.setFpJym(ptfpTaxJson.getDataJson().getJym());
                    dzfpInvoice.setJqbh(ptfpTaxJson.getDataJson().getJqbh());
                    try {
                        dzfpInvoice.setKprq(DateTimeUtil.getDateTimeTODate(ptfpTaxJson.getDataJson().getKprq()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dzfpInvoice.setHjje(new BigDecimal((ptfpTaxJson.getDataJson().getJe())));
//              dzfpInvoice.setSlv();
                    dzfpInvoice.setHjse(new BigDecimal(ptfpTaxJson.getDataJson().getSe()));
                    dzfpInvoice.setJshjxx(new BigDecimal(ptfpTaxJson.getDataJson().getJshjxx()));
                    dzfpInvoice.setGfsh(ptfpTaxJson.getDataJson().getGfsbh() != null ? ptfpTaxJson.getDataJson().getGfsbh() : "");
                    dzfpInvoice.setGfmc(ptfpTaxJson.getDataJson().getGfmc() != null ? ptfpTaxJson.getDataJson().getGfmc() : "");
                    dzfpInvoice.setGfdzDh(ptfpTaxJson.getDataJson().getGfdzdh() != null ? ptfpTaxJson.getDataJson().getGfdzdh() : "");
                    dzfpInvoice.setGfyhmcYhzh(ptfpTaxJson.getDataJson().getGfyhzh() != null ? ptfpTaxJson.getDataJson().getGfyhzh() : "");
                    dzfpInvoice.setXfsh(ptfpTaxJson.getDataJson().getXfsbh() != null ? ptfpTaxJson.getDataJson().getXfsbh() : "");
                    dzfpInvoice.setXfmc(ptfpTaxJson.getDataJson().getXfmc() != null ? ptfpTaxJson.getDataJson().getXfmc() : "");
                    dzfpInvoice.setXfdzDh(ptfpTaxJson.getDataJson().getXfdzdh() != null ? ptfpTaxJson.getDataJson().getXfdzdh() : "");
                    dzfpInvoice.setXfyhmcYhzh(ptfpTaxJson.getDataJson().getXfyhzh() != null ? ptfpTaxJson.getDataJson().getXfyhzh() : "");
                    dzfpInvoice.setZfbz(ptfpTaxJson.getDataJson().getZfbz() != null ? ptfpTaxJson.getDataJson().getZfbz() : "");
                    dzfpInvoice.setBz(ptfpTaxJson.getDataJson().getBz() != null ? ptfpTaxJson.getDataJson().getBz() : "");
                    dzfpInvoice.setFphm(ptfpTaxJson.getFphm());
                    dzfpInvoice.setFpdm(ptfpTaxJson.getFpdm());
                    dzfpInvoice.setFplx(type);
                    List<com.xiaour.spring.boot.jsonBean.ptfp.Mx> list = ptfpTaxJson.getDataJson().getMx();
                    dzfpInvoiceMapper.insertSelective(dzfpInvoice);//新增发票主体信息
                    List<DzfpInvoiceQd> dzfpInvoiceQdList = new ArrayList<>();
                    if (list.size() != 0) {
                        for (com.xiaour.spring.boot.jsonBean.ptfp.Mx mx : list) {
                            DzfpInvoiceQd dzfpInvoiceQd = new DzfpInvoiceQd();
                            dzfpInvoiceQd.setFpdm(dzfpInvoice.getFpdm());
                            dzfpInvoiceQd.setFphm(dzfpInvoice.getFphm());
                            dzfpInvoiceQd.setWpMc(mx.getMc());
                            dzfpInvoiceQd.setWpXh(mx.getGgxh());
                            dzfpInvoiceQd.setWpDw(mx.getDw());
                            String sl = mx.getSl();
                            if (sl.equals(" ") || sl.equals("")) {
                                sl = "1";
                            }
                            String dj = mx.getDj();
                            if (dj.equals(" ") || dj.equals("")) {
                                dj = mx.getJe();
                            }
                            dzfpInvoiceQd.setSl(new BigDecimal(sl));
                            dzfpInvoiceQd.setDj(new BigDecimal(dj).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                            dzfpInvoiceQd.setJe(new BigDecimal(mx.getJe() != " " ? mx.getJe() : "0.0"));
                            dzfpInvoiceQd.setSlv(new BigDecimal(mx.getSlv()));
                            dzfpInvoiceQd.setSe(new BigDecimal(mx.getSe()));
                            dzfpInvoiceQd.setXfNsrsbh(dzfpInvoice.getXfsh());
                            dzfpInvoiceQdList.add(dzfpInvoiceQd);
                            dzfpInvoiceQdMapper.insertSelective(dzfpInvoiceQd);//新增明细

                        }

                    }
                    hashMap.put("invoice", dzfpInvoice);
                    hashMap.put("invoice_mx", dzfpInvoiceQdList);
                }

            } else if (Integer.parseInt(type) == 10) {//增值税普通发票（电子）
                try {
                    dzfpJson = JSON.parseObject(resultJson, DzfpJson.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Boolean isok = dzfpJson.getIsok();
                if (isok == false) {
                    result.setCode(ResultCode.NO_RETURN);
                    String msg = dzfpJson.getMsg();
                    if (msg.contains("null")) {
                        result.setMessage("请核对查验数据是否正确");
                    } else {
                        result.setMessage(dzfpJson.getMsg());
                    }
                    return result;
                } else {
                    DzfpTxaJson dzfpTxaJson = dzfpJson.getData();
                    if (dzfpTxaJson.getDataJson() == null
//                        &&dzfpTxaJson.toString().indexOf("false")>0
                            ) {
                        result.setCode(ResultCode.NO_ACCESS);
                        result.setMessage("网络繁忙，请稍后再试");
                        result.setData(hashMap);
                        return result;
                    }
                    if (!dzfpTxaJson.getFplx().equals(type)) {
                        result.setCode(ResultCode.NO_FPZL);
                        result.setMessage("发票种类不正确，请确认");
                        result.setData(hashMap);
                        return result;
                    }
                    dzfpInvoice.setFpcc(dzfpTxaJson.getFpcc());

                    dzfpInvoice.setFpJym(dzfpTxaJson.getDataJson().getJym());
                    dzfpInvoice.setJqbh(dzfpTxaJson.getDataJson().getJqbh());
                    try {
                        dzfpInvoice.setKprq(DateTimeUtil.getDateTimeTODate(dzfpTxaJson.getDataJson().getKprq()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dzfpInvoice.setHjje(new BigDecimal((dzfpTxaJson.getDataJson().getJe())));
//              dzfpInvoice.setSlv();
                    dzfpInvoice.setHjse(new BigDecimal(dzfpTxaJson.getDataJson().getSe()));
                    dzfpInvoice.setJshjxx(new BigDecimal(dzfpTxaJson.getDataJson().getJshjxx()));
                    dzfpInvoice.setGfsh(dzfpTxaJson.getDataJson().getGfsbh() != null ? dzfpTxaJson.getDataJson().getGfsbh() : "");
                    dzfpInvoice.setGfmc(dzfpTxaJson.getDataJson().getGfmc() != null ? dzfpTxaJson.getDataJson().getGfmc() : "");
                    dzfpInvoice.setGfdzDh(dzfpTxaJson.getDataJson().getGfdzdh() != null ? dzfpTxaJson.getDataJson().getGfdzdh() : "");
                    dzfpInvoice.setGfyhmcYhzh(dzfpTxaJson.getDataJson().getGfyhzh() != null ? dzfpTxaJson.getDataJson().getGfyhzh() : "");
                    dzfpInvoice.setXfsh(dzfpTxaJson.getDataJson().getXfsbh() != null ? dzfpTxaJson.getDataJson().getXfsbh() : "");
                    dzfpInvoice.setXfmc(dzfpTxaJson.getDataJson().getXfmc() != null ? dzfpTxaJson.getDataJson().getXfmc() : "");
                    dzfpInvoice.setXfdzDh(dzfpTxaJson.getDataJson().getXfdzdh() != null ? dzfpTxaJson.getDataJson().getXfdzdh() : "");
                    dzfpInvoice.setXfyhmcYhzh(dzfpTxaJson.getDataJson().getXfyhzh() != null ? dzfpTxaJson.getDataJson().getXfyhzh() : "");
                    dzfpInvoice.setZfbz(dzfpTxaJson.getDataJson().getZfbz() != null ? dzfpTxaJson.getDataJson().getZfbz() : "");
                    dzfpInvoice.setBz(dzfpTxaJson.getDataJson().getBz() != null ? dzfpTxaJson.getDataJson().getBz() : "");
                    dzfpInvoice.setFphm(dzfpTxaJson.getFphm());
                    dzfpInvoice.setFpdm(dzfpTxaJson.getFpdm());
                    dzfpInvoice.setFplx(type);
                    List<Mx> list = dzfpTxaJson.getDataJson().getMx();
                    dzfpInvoiceMapper.insertSelective(dzfpInvoice);//新增发票主体信息

                    List<DzfpInvoiceQd> dzfpInvoiceQdList = new ArrayList<>();
                    if (list.size() != 0) {
                        for (Mx mx : list) {
                            DzfpInvoiceQd dzfpInvoiceQd = new DzfpInvoiceQd();
                            dzfpInvoiceQd.setFpdm(dzfpInvoice.getFpdm());
                            dzfpInvoiceQd.setFphm(dzfpInvoice.getFphm());
                            dzfpInvoiceQd.setWpMc(mx.getMc());
                            dzfpInvoiceQd.setWpXh(mx.getGgxh());
                            dzfpInvoiceQd.setWpDw(mx.getDw());
                            String sl = mx.getSl();
                            if (sl.equals(" ") || sl.equals("")) {
                                sl = "1";
                            }
                            String dj = mx.getDj();
                            if (dj.equals(" ") || dj.equals("")) {
                                dj = mx.getJe();
                            }
                            dzfpInvoiceQd.setSl(new BigDecimal(sl));
                            dzfpInvoiceQd.setDj(new BigDecimal(dj).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                            dzfpInvoiceQd.setJe(new BigDecimal(mx.getJe()));
                            dzfpInvoiceQd.setSlv(new BigDecimal(mx.getSlv()));
                            dzfpInvoiceQd.setSe(new BigDecimal(mx.getSe()));
                            dzfpInvoiceQd.setXfNsrsbh(dzfpInvoice.getXfsh());
                            dzfpInvoiceQdList.add(dzfpInvoiceQd);
                            dzfpInvoiceQdMapper.insertSelective(dzfpInvoiceQd);//新增明细

                        }

                    }
                    hashMap.put("invoice", dzfpInvoice);
                    hashMap.put("invoice_mx", dzfpInvoiceQdList);
                }
            } else if (Integer.parseInt(type) == 11) {//增值税普通发票（卷票）
                try {
                    jsfpJson = JSON.parseObject(resultJson, JsfpJson.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Boolean isok = jsfpJson.getIsok();
                if (isok == false) {
                    result.setCode(ResultCode.NO_RETURN);
                    String msg = jsfpJson.getMsg();
                    if (msg.contains("null")) {
                        result.setMessage("请核对查验数据是否正确");
                    } else {
                        result.setMessage(jsfpJson.getMsg());
                    }
                    return result;
                } else {
                    JsfpTxaJson jsfpTaxJson = jsfpJson.getData();
                    if (jsfpTaxJson.getDataJson() == null) {
                        result.setCode(ResultCode.NO_ACCESS);
                        result.setMessage("网络繁忙，请稍后再试");
                        result.setData(hashMap);
                        return result;
                    }
                    if (!jsfpTaxJson.getFplx().equals(type)) {
                        result.setCode(ResultCode.NO_FPZL);
                        result.setMessage("发票种类不正确，请确认");
                        result.setData(hashMap);
                        return result;
                    }
                    dzfpInvoice.setFpcc(jsfpTaxJson.getFpcc());
                    dzfpInvoice.setFpJym(jsfpTaxJson.getDataJson().getJym());
                    try {
                        dzfpInvoice.setKprq(DateTimeUtil.getDateTimeTODate(jsfpTaxJson.getDataJson().getKprq()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dzfpInvoice.setGfmc(jsfpTaxJson.getDataJson().getGfmc() != null ? jsfpTaxJson.getDataJson().getGfmc() : "");
                    dzfpInvoice.setGfsh(jsfpTaxJson.getDataJson().getGfsh() != null ? jsfpTaxJson.getDataJson().getGfsh() : "");
                    dzfpInvoice.setXfmc(jsfpTaxJson.getDataJson().getXfmc() != null ? jsfpTaxJson.getDataJson().getXfmc() : "");
                    dzfpInvoice.setXfsh(jsfpTaxJson.getDataJson().getXfsh() != null ? jsfpTaxJson.getDataJson().getXfsh() : "");
                    dzfpInvoice.setZfbz(jsfpTaxJson.getDataJson().getZfbz() != null ? jsfpTaxJson.getDataJson().getZfbz() : "");
                    dzfpInvoice.setShy(jsfpTaxJson.getDataJson().getShy() != null ? jsfpTaxJson.getDataJson().getShy() : "");
                    dzfpInvoice.setBz(jsfpTaxJson.getDataJson().getBz() != null ? jsfpTaxJson.getDataJson().getBz() : "");
                    dzfpInvoice.setJqbh(jsfpTaxJson.getDataJson().getSbbh() != null ? jsfpTaxJson.getDataJson().getSbbh() : "");
                    dzfpInvoice.setJshjxx(new BigDecimal(jsfpTaxJson.getDataJson().getJshj()));
                    dzfpInvoice.setFphm(jsfpTaxJson.getFphm());
                    dzfpInvoice.setFpdm(jsfpTaxJson.getFpdm());
                    dzfpInvoice.setFplx(type);
                    List<com.xiaour.spring.boot.jsonBean.jsfp.Mx> list = jsfpTaxJson.getDataJson().getMx();
                    dzfpInvoiceMapper.insertSelective(dzfpInvoice);//新增发票主体信息
                    List<DzfpInvoiceQd> dzfpInvoiceQdList = new ArrayList<>();
                    if (list.size() != 0) {
                        for (com.xiaour.spring.boot.jsonBean.jsfp.Mx mx : list) {
                            DzfpInvoiceQd dzfpInvoiceQd = new DzfpInvoiceQd();
                            dzfpInvoiceQd.setFpdm(dzfpInvoice.getFpdm());
                            dzfpInvoiceQd.setFphm(dzfpInvoice.getFphm());
                            dzfpInvoiceQd.setWpMc(mx.getMc());
                            String sl = mx.getSl();
                            if (sl.equals(" ") || sl.equals("")) {
                                sl = "1";
                            }
                            String dj = mx.getDj();
                            if (dj.equals(" ") || dj.equals("")) {
                                dj = mx.getJe();
                            }
                            dzfpInvoiceQd.setSl(new BigDecimal(sl));
                            dzfpInvoiceQd.setDj(new BigDecimal(dj).setScale(2, BigDecimal.ROUND_HALF_DOWN));
                            dzfpInvoiceQd.setJe(new BigDecimal(mx.getJe()));
                            dzfpInvoiceQd.setXfNsrsbh(dzfpInvoice.getXfsh());
                            dzfpInvoiceQdList.add(dzfpInvoiceQd);
                            dzfpInvoiceQdMapper.insertSelective(dzfpInvoiceQd);//新增明细
                        }
                    }
                    hashMap.put("invoice", dzfpInvoice);
                    hashMap.put("invoice_mx", dzfpInvoiceQdList);
                }
            } else if (Integer.parseInt(type) == 14) {//增值税电子普通发票

            } else if (Integer.parseInt(type) == 15) {//二手车销售统一发票

            }
        }
        result.setData(hashMap);
        return result;
    }
}
