package com.xiaour.spring.boot.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HttpRequestUtils {

    public static String getRequest(String url){

        URL urlPath;
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = null;

        try {
            //发送请求
            urlPath = new URL(url);
            httpURLConnection = (HttpURLConnection)urlPath.openConnection();
            httpURLConnection.connect();

            //请求返回的数据
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static String postRequest(String url,String data){

        URL urlPath;
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        OutputStreamWriter outputStreamWriter = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            urlPath = new URL(url);
            httpURLConnection = (HttpURLConnection)urlPath.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            //添加请求体
            outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
            outputStreamWriter.write(data);
            outputStreamWriter.flush();

            //请求返回的数据
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public static String multipartRequest(String url,String fileName,InputStream inputStream){

        URL urlPath;
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            urlPath = new URL(url);
            httpURLConnection = (HttpURLConnection)urlPath.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            //添加请求头
            String bound = UUID.randomUUID().toString().replace("-","");
            httpURLConnection.setRequestProperty("Content-Type","multipart/form-data; boundary="+ bound);

            //添加请求体,必须直接传byte，StringBuilder转byte不可用
            outputStream = httpURLConnection.getOutputStream();
            String boundary = "--"+bound+"\r\n";
            outputStream.write(boundary.getBytes());
            String disposition = "Content-Disposition:form-data; name=\"media\"; filename=\""+fileName+"\"\r\n";
            outputStream.write(disposition.getBytes());
            outputStream.write("Content-Type:application/octet-stream\r\n\r\n".getBytes());
            //文件字节流
            int lenth;
            byte[] bytes = new byte[1024];
            while ((lenth = inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,lenth);
            }
            String foot = "\r\n--"+bound+"--\r\n";
            outputStream.write(foot.getBytes());
            outputStream.flush();
            inputStream.close();

            //请求返回的数据
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }






}
