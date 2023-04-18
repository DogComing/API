package com.platform.util;

import com.platform.utils.CharUtil;
import com.platform.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 生成订单的编号order_sn
     */
    public static String generateOrderNumber() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String timeStr = DateUtils.format(cal.getTime(), DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS);
        return timeStr + CharUtil.getRandomNum(6);
    }

    /**
     * 获取昨天 年月日 yyyy-MM-dd
     * @return
     */
    public static String getYesterday(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        return new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 获取今天 年月日 yyyy-MM-dd
     * @return
     */
    public static String getToday(Date currentTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getStringDate() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /***
     * @Description: 获取随机字符串
     * @Param: [length]
     * @return: java.lang.String
     * @Author: Yuan
     * @Date: 2019/11/21
     */
    public static String getRandomStringByLength(int length) {

        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * string 转 date
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static Date strFormatDate(String dateTime) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateTime);
    }
}
