package com.xiaour.spring.boot.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zccx on 2016/12/28.
 */
public class IpUtils {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getTxt(HttpServletRequest request) throws IOException {
        String path = this.getClass().getResource("/").getPath();
        request.setCharacterEncoding("utf-8");
        // 参数Map
        Map properties = request.getParameterMap();
//        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        String codeJson = JSON.toJSONString(returnMap);
//        InetAddress address1 = InetAddress.getByName("www.bjzsxx.com");//获取的是该网站的ip地址
//        String ip = address1.getHostAddress();
        //获取本机的ip地址和域名
        InetAddress ia = InetAddress.getLocalHost();
        String ip = ia.getHostAddress();
        LogRzUtils.outLogTxt(path + "/log/", ip, codeJson);
        File file = new File(path + "ip.txt");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer b = new StringBuffer();
            String s = "";
            while ((s = reader.readLine()) != null) {
                b.append(s);
            }
            reader.close();
            if (b.toString().contains(ip)) {
                return "1";
            } else {
                return "2";
            }

        } catch (Exception e) {
            return "2";
        }
    }

}
