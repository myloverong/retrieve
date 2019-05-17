package com.xiaour.spring.boot.service;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class test {


    @Test
    public void aa() throws FileNotFoundException, URISyntaxException, UnsupportedEncodingException {
        System.out.println(path());
    }

    public String path() throws URISyntaxException, FileNotFoundException, UnsupportedEncodingException {
        String path = getClass().getResource("/").getPath();

        String fis = java.net.URLDecoder.decode(path, "utf-8");
        System.out.println(fis);
        return path;
    }
}
