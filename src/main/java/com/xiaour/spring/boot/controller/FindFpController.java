package com.xiaour.spring.boot.controller;


import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.xiaour.spring.boot.core.Result;
import com.xiaour.spring.boot.core.ResultCode;
import com.xiaour.spring.boot.entity.DzfpInvoice;
import com.xiaour.spring.boot.mapper.DzfpInvoiceMapper;
import com.xiaour.spring.boot.service.DzfpInvoiceService;
import com.xiaour.spring.boot.service.RedisService;
import com.xiaour.spring.boot.utils.IpUtils;
import com.xiaour.spring.boot.utils.ReadConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
     
	 
	 

/**
 * Created by xiaour on 2017/4/19.
 */
@RestController
public class FindFpController {

    @Autowired
    private RedisService redisService;


    @Autowired
    private DzfpInvoiceMapper dzfpInvoiceMapper;

    @Resource
    private DzfpInvoiceService dzfpInvoiceService;

    @RequestMapping(value = "/index")
    public String index() {
        return "hello world";
    }

    @ApiOperation(value = "发票查验接口", notes = "管理员登录")
    @RequestMapping(value = "/getfp", method = RequestMethod.POST)
    public Result<Map<String, Object>> setDzfp(
            HttpServletRequest req, HttpServletResponse resp,
            @ApiParam("发票代码") @RequestParam(value = "fpdm", required = true) String fpdm,
            @ApiParam("发票号码") @RequestParam(value = "fphm", required = true) String fphm,
            @ApiParam("开票日期") @RequestParam(value = "kprq", required = true) String kprq,
            @ApiParam("开票不含税金额合计/校验码后六位") @RequestParam(value = "jynum", required = true) String kjje,
            @ApiParam("发票种类:01-增值税专用发票\n" +
                    "02-货物运输业增值税专用发票\n" +
                    "03-机动车销售统一发票\n" +
                    "04-增值税普通发票\n" +
                    "10-增值税普通发票（电子）\n" +
                    "11-增值税普通发票（卷票）\n" +
                    "14-增值税电子普通发票\n" +
                    "15-二手车销售统一发票") @RequestParam(value = "type", required = true) String type
    ) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        IpUtils ipUtils = new IpUtils();
        String pdip = ipUtils.getTxt(req);
        if (pdip.equals("2")) {
            Result<Map<String, Object>> result = new Result<>();
            result.setCode(ResultCode.NO_QUANXIAN);
            result.setMessage("暂无权限查验");
            return result;
        }
        DzfpInvoice dzfpInvoice = new DzfpInvoice();
        dzfpInvoice.setFpdm(fpdm);
        dzfpInvoice.setFphm(fphm);
        dzfpInvoice.setFplx(type);
        Result<Map<String, Object>> o = dzfpInvoiceService.findfp(dzfpInvoice, kprq, kjje, type);
        return o;
    }

    /**
     * 向redis存储值
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    @RequestMapping("/set")
    public String set(String key, String value) throws Exception {

        redisService.set(key, value);
        return "success";
    }

    /**
     * 获取redis中的值
     *
     * @param key
     * @return
     */
    @RequestMapping("/get")
    public String get(String key) {
        try {
            return redisService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void main(String[] args) {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("id", "编号");
        keyMap.put("name", "名称");

        String[] cnCloumn = {"编号", "名称"};

        System.out.println(Arrays.asList(convertMap(keyMap, cnCloumn)));

    }

    public static String[] convertMap(Map<String, Object> keyMap, String[] dataList) {

        for (int i = 0; i < dataList.length; i++) {

            for (Map.Entry<String, Object> m : keyMap.entrySet()) {
                if (m.getValue().equals(dataList[i])) {
                    dataList[i] = m.getKey();
                }
            }
        }

        return dataList;
    }


    public static String getName(String name, String add) {
        return null;
    }

    public static void testGetClassName() {
        // 方法1：通过SecurityManager的保护方法getClassContext()
        String clazzName = new SecurityManager() {
            public String getClassName() {
                return getClassContext()[1].getName();
            }
        }.getClassName();
        System.out.println(clazzName);
        // 方法2：通过Throwable的方法getStackTrace()
        String clazzName2 = new Throwable().getStackTrace()[1].getClassName();
        System.out.println(clazzName2);
        // 方法3：通过分析匿名类名称()
        String clazzName3 = new Object() {
            public String getClassName() {
                String clazzName = this.getClass().getName();
                return clazzName.substring(0, clazzName.lastIndexOf('$'));
            }
        }.getClassName();
        System.out.println(clazzName3);
        //方法4：通过Thread的方法getStackTrace()
        String clazzName4 = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println(clazzName4);
    }


}
