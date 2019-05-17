package com.xiaour.spring.boot.utils;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangxiangfei 2018-09-03.
 */
public class LogRzUtils {

    public static String readFileContent(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String outLogTxt(String s, String ip, String codeJson) throws IOException {
        File file = new File(s);
        if (!file.exists()) {
            file.mkdirs();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filename = sdf.format(date);
        String filePath = s + filename;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String newDate = sd.format(date);
        FileWriter fw = new FileWriter(filePath + ".txt", true);
        String context = newDate + "\t" + "---" + ip + "\t" + "    参数 ---" + codeJson + " \t" + "\r\n";
        fw.write(context);
        fw.flush();
        fw.close();
        return "";

    }

    /**
     * unicode转换成中文
     *
     * @param str
     * @return
     */
    public static String unicodeToChina(String str) {
        Charset set = Charset.forName("UTF-16");
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
}
