package com.xiaour.spring.boot.utils;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ReadConfigFile {
    private static final String fileName= "config.properties";


    /**
     * 获取properties配置文件里指定配置项的值
     * @param id 要读取的配置项id
     * @return
     */
    public  String readConfigProperties(String id)
    {
        Properties prop = new Properties();
        InputStream stream = null;
        String configVal = "";
        String path = getClass().getResource("/").getPath();
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            stream = new BufferedInputStream(new FileInputStream(new File(path+fileName)));
            prop.load(stream);
            Set config_info = prop.keySet();
            Iterator it = config_info.iterator();
            while(it.hasNext()){
                /*String id = (String)it.next();
                String value = p.getProperty(id);
                System.out.println(id+":="+value);*/
                if (id.equals(it.next()))
                {
                    configVal = prop.getProperty(id);
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return configVal;
    }

    /**
     * 获取properties配置文件里指定配置项的值
     * @param fileName 要读取的配置文件名
     * @param id 要读取的配置项id
     * @return
     */
    public String readPropertiesFile(String fileName, String id)
    {
        Properties prop = new Properties();
        InputStream stream = null;
        String configVal = "";
        String path = getClass().getResource("/").getPath();

        try {
            stream = new BufferedInputStream(new FileInputStream(new File(path+fileName)));
            prop.load(stream);
            Set config_info = prop.keySet();
            Iterator it = config_info.iterator();
            while(it.hasNext()){
                /*String id = (String)it.next();
                String value = p.getProperty(id);
                System.out.println(id+":="+value);*/
                if (id.equals(it.next()))
                {
                    configVal = prop.getProperty(id);
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return configVal;
    }
}
