package com.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.platform.config.ConstantConfig.*;

public class EffectiveTimeUtil {

    /**
     * 上午下午两个时间段
     * @param nowTime
     * @param amBeginTime
     * @param amEndTime
     * @param pmBeginTime
     * @param pmEndTime
     * @return boolean
     */
    //判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
    public static boolean timeCalendar(Date nowTime, Date amBeginTime, Date amEndTime, Date pmBeginTime, Date pmEndTime) {

        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar amBegin = Calendar.getInstance();
        amBegin.setTime(amBeginTime);//上午开始时间
        Calendar pmBegin = Calendar.getInstance();
        pmBegin.setTime(pmBeginTime);//下午开始时间
        //设置结束时间
        Calendar amEnd = Calendar.getInstance();
        amEnd.setTime(amEndTime);//上午结束时间
        Calendar pmEnd = Calendar.getInstance();
        pmEnd.setTime(pmEndTime);//下午结束时间
        //处于开始时间之后，和结束时间之前的判断
        if((date.after(amBegin) && date.before(amEnd)) || (date.after(pmBegin) && date.before(pmEnd))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
     * @return
     */
    public static boolean timeCalendar() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String nowTimeStr = sdf.format(date);
        Date beginTime = null;
        Date endTime = null;
        Date nowTime = null;
        try {
            nowTime = sdf.parse(nowTimeStr);
            beginTime = sdf.parse(gameBeginTime);
            endTime = sdf.parse(gameEndTime);

            // 设置当前时间
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(nowTime);
            // 设置开始时间
            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime); // 开始时间
            // 设置结束时间
            Calendar end = Calendar.getInstance();
            end.setTime(endTime); // 上午结束时间
            // 处于开始时间之后，和结束时间之前的判断
            if((currentDate.after(begin) && currentDate.before(end))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在时间段之后
     * @param nowTime
     * @param beginTime
     * @return boolean
     */
    //判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
    public static boolean afterTimeCalendar(Date nowTime, Date beginTime) {

        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);//开始时间
        //处于开始时间之后，和结束时间之前的判断
        if(( date.after(begin))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 在时间段之前
     * @param nowTime
     * @param beginTime
     * @return boolean
     */
    //判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
    public static boolean beforeTimeCalendar(Date nowTime, Date beginTime) {

        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);//开始时间
        //处于开始时间之后，和结束时间之前的判断
        if(( date.before(begin))) {
            return true;
        } else {
            return false;
        }
    }

}
