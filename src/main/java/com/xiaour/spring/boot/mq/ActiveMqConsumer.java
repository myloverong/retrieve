package com.xiaour.spring.boot.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaour.spring.boot.utils.HttpRequestUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: huangxiangfei
 * @CreateDate: 2019/3/19$ 18:20$
 */
@Component
public class ActiveMqConsumer {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Resource
    private Queue queue;


    @JmsListener(destination = "activemqmq")
    public void messageConumer(String text) throws IOException {
        String fileProductSeriesPath = this.getClass().getResource("/").getPath();

        if (text != null) {
            Map<String, Object> paramMap = ActiveMqConsumer.str2Map(text);
            try {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq1=====text:" + paramMap.get("data") + "&type=" + paramMap.get("type"));
                String sendUrl = paramMap.get("sendUrl").toString();
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq2=====sendUrl:" + sendUrl);
                String result = HttpRequestUtils.postRequest(sendUrl, paramMap.get("data").toString());
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq3=====result:" + result);
                if (result == null || result.equals("")) {
                    ProducterService producterService = new ProducterService();
                    producterService.sendMessage1(text, this.jmsMessagingTemplate);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = JSONObject.parseObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.get("code") != null) {
                            if (!jsonObject.getString("code").equals("1")) {
                                ProducterService producterService = new ProducterService();
                                producterService.sendMessage1(text, this.jmsMessagingTemplate);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq4=====error:" + e);
            }
        }
    }

    @JmsListener(destination = "myactivemq-backmq")
    public void messageConumerback(String text) throws IOException {
        String fileProductSeriesPath = this.getClass().getResource("/").getPath();

        if (text != null) {
            Map<String, Object> paramMap = ActiveMqConsumer.str2Map(text);
            try {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq4=====text:" + paramMap.get("data") + "&type=" + paramMap.get("type"));
                String sendUrl = paramMap.get("sendUrl").toString();
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq5=====sendUrl:" + sendUrl);
                String result = HttpRequestUtils.postRequest(sendUrl, paramMap.get("data").toString());
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq6=====result:" + result);
                if (result == null || result.equals("")) {
                    ProducterService producterService = new ProducterService();
                    producterService.sendMessage2(text, this.jmsMessagingTemplate);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = JSONObject.parseObject(result);
                    if (jsonObject != null) {
                        if (jsonObject.get("code") != null) {
                            if (!jsonObject.getString("code").equals("1")) {
                                ProducterService producterService = new ProducterService();
                                producterService.sendMessage2(text, this.jmsMessagingTemplate);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemqback4=====error:" + e);
            }
        }
    }

    @JmsListener(destination = "myactivemq-backtwomq")
    public void messageConumerbacktwo(String text) throws IOException {
        String fileProductSeriesPath = this.getClass().getResource("/").getPath();

        if (text != null) {
            Map<String, Object> paramMap = ActiveMqConsumer.str2Map(text);
            try {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq7=====text:" + paramMap.get("data") + "&type=" + paramMap.get("type"));
                String sendUrl = paramMap.get("sendUrl").toString();
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq8=====sendUrl:" + sendUrl);
                String result = HttpRequestUtils.postRequest(sendUrl, paramMap.get("data").toString());
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq9=====result:" + result);
            } catch (Exception e) {
                ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemqbacktwo4=====error:" + e);
            }
        }
    }

    public static String outLogTxt(String path, String loadname) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        path = path + sdf.format(date) + "/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filename = sdf.format(date);
        String filePath = path + filename;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String newDate = sd.format(date);
        FileWriter fw = new FileWriter(filePath + ".txt", true);
        String context = newDate + "\t" + loadname + "\t" + "\r\n";
        context = ActiveMqConsumer.unicodeToChina(context);
        fw.write(context);
        fw.flush();
        fw.close();
        return "";
    }

    public static String unicodeToChina(String str) {
        Charset set = Charset.forName("UTF-8");
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(str);
        int start = 0;
        int start2 = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find(start)) {
            start2 = m.start();
            if (start2 > start) {
                String seg = str.substring(start, start2);
                sb.append(seg);
            }
            String code = m.group(1);
            int i = Integer.valueOf(code, 16);
            byte[] bb = new byte[4];
            bb[0] = (byte) ((i >> 8) & 0xFF);
            bb[1] = (byte) (i & 0xFF);
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append(String.valueOf(set.decode(b)).trim());
            start = m.end();
        }
        start2 = str.length();
        if (start2 > start) {
            String seg = str.substring(start, start2);
            sb.append(seg);
        }
        return sb.toString();
    }

    public static Map<String, Object> str2Map(String str) {
        return (Map<String, Object>) JSON.parseObject(str, Map.class);
    }

//    public static void main(String[] args) {
//        String text = "invguid=d0327d44663c11e997240cc47a282e4a&kprId=null&status=2&reson=&fpdm=044031900111&fphm=27248540&url=http://invoicefileservice.bjzsxx.com/440300/201903/11/91440300MA5DHCEA4J/201904/24/1/04403190011127248540.pdf&email=5298725@qq.com&kptime=2019-04-24 10:58:45&sendUrl=http://sjdc.srs-soft.com.cn/srs_zfb_dc/InvoiceServlet?sz=8&type=1";
//        String text8 = text.substring(0, text.lastIndexOf("&"));
//        String sendUrl = text8.substring(text8.lastIndexOf("=") + 1);
////        ActiveMqConsumer.outLogTxt(fileProductSeriesPath + "/log/", "backActivemq2=====sendUrl:" + sendUrl);
//        String text1 = text8.substring(0, text8.lastIndexOf("&"));
//        System.out.println(text);
//
//    }

}
