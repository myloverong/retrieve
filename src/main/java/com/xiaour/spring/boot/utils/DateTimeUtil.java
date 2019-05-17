package com.xiaour.spring.boot.utils;

import org.apache.poi.ss.usermodel.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static void main(String[] args) {
        try {
            System.out.println(getDateTimeTODate("20180818"));
            System.out.println(getDateTimeTOString(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * date转String
     *
     * @param date
     * @return
     */
    static String getDateTimeTOString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * String 转 Date
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateTimeTODate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(date);
    }
}
