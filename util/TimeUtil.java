package com.threetree.baseproject.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 时间格式工具类
 *
 */
public class TimeUtil {
    private TimeUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得年
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYear(long format) {
        return new SimpleDateFormat("yyyy年").format(new Date(format));
    }

    /**
     * 获得月
     */
    @SuppressLint("SimpleDateFormat")
    public static String getMonth(long format) {
        return new SimpleDateFormat("MM月").format(new Date(format));
    }

    /**
     * 获得日
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDay(long format) {
        return new SimpleDateFormat("dd日").format(new Date(format));
    }

    /**
     * 获得时
     */
    @SuppressLint("SimpleDateFormat")
    public static String getHours(long format) {
        return new SimpleDateFormat("HH时").format(new Date(format));
    }

    /**
     * 获得分
     */
    @SuppressLint("SimpleDateFormat")
    public static String getMinutes(long format) {
        return new SimpleDateFormat("mm分").format(new Date(format));
    }

    /**
     * 获得秒
     */
    @SuppressLint("SimpleDateFormat")
    public static String getSeconds(long format) {
        return new SimpleDateFormat("ss秒").format(new Date(format));
    }

    /**
     * 格式化时间，获得年月格式为（YYYY年MM月）
     *
     * @param format
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatYYYYMM(String format) {
        StringBuffer time = new StringBuffer();
        time.append(format.substring(0, 4)).append("年").append(format.substring(4)).append("月");
        return time.toString();
    }

    /**
     * 获得年月日格式为（yyyy/MM/dd）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getY_M_D_Type1(long format) {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(format));
    }

    /**
     * 获得年月日格式为（yyyy年MM月dd日）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getY_M_D_Type2(long format) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(format));
    }

    /**
     * 获得时分秒格式为（HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getH_M_S(long format) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(format));
    }

    /**
     * 获得时分格式为（HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getH_M(long format) {
        return new SimpleDateFormat("HH:mm").format(new Date(format));
    }

    /**
     * 获得月日时分格式为（MM/dd HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getM_D_H_M(long format) {
        return new SimpleDateFormat("MM/dd HH:mm").format(new Date(format));
    }

    /**
     * 获得月日时分格式为（MM-dd  HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getM_D__H_M(long format) {
        return new SimpleDateFormat("MM-dd  HH:mm").format(new Date(format));
    }

    /**
     * 获得年月日时分秒（yyyy/MM/dd HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeType(long format) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(format));
    }

    /**
     * 获得年月日时分秒（yyyy-MM-dd HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeType2(long format) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(format));
    }

    /**
     * 自定义时间格式
     *
     * @param format
     * @param format "yyyy-MM-dd HH:mm:ss"当前系统时间的时间格式
     * @return
     */
    public static String getFullTime(String time, String format) {
        long mTime = 0;
        try {
            mTime = Long.parseLong(time);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("数字格式异常:" + e.getMessage());
        }
        int length = (mTime + "").length();
        switch (length) {
            case 10:
                return new SimpleDateFormat(format).format(new Date(mTime * 1000));
            case 13:
                return new SimpleDateFormat(format).format(new Date(mTime));
            default:
                return "1970-01-01 08:00:00";
        }
    }

    /**
     * 自定义时间格式
     *
     * @param format
     * @param format "yyyy-MM-dd HH:mm:ss"当前系统时间的时间格式
     * @return
     */
    public static String getFullTime(long time, String format) {
        int length = (time + "").length();
        switch (length) {
            case 10:
                return new SimpleDateFormat(format).format(new Date(time * 1000));
            case 13:
                return new SimpleDateFormat(format).format(new Date(time));
            default:
                return new SimpleDateFormat(format).format(new Date(time));
        }
    }

    /**
     * 将字符串时间转化成时间戳(time的格式与format的格式一样)
     *
     * @param time   1970/01/01 08:00:00
     * @param format yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static long getTimeTransformation(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            throw new UnsupportedOperationException("解析异常" + e.getMessage());
        }
        return (date.getTime());
    }



    /**
     * 将秒转换成几时几分几秒
     *
     * @param <T>
     * @param second 秒
     * @return
     */
    public static <T> String formatSecond(T second) {
        String html = "0s";
        if (second != null) {
            Double s = Double.parseDouble(second + "");
            String format;
            Object[] array;
            Integer hours = (int) (s / (60 * 60));
            Integer minutes = (int) (s / 60 - hours * 60);
            Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
            if (hours > 0) {
                format = "%1$,02d:%2$,02d:%3$,02d";
                array = new Object[]{hours, minutes, seconds};
            } else if (minutes > 0) {
                format = "%1$,02d:%2$,02d";
                array = new Object[]{minutes, seconds};
            } else {
                format = "%1$,ds";
                array = new Object[]{seconds};
            }
            html = String.format(format, array);
        }
        return html;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算开始时间与系统当前时间之间相差的分钟数
     *
     * @param smdate 开始时间
     * @return 相差分钟数
     * @throws ParseException
     */
    public static int minuteBetween(long smdate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date endtime = format.parse(getH_M_S(smdate));  //开始抢标时间
        Date starttime = format.parse(format.format(new Date()));       //系统当前时间
        long seconds = endtime.getTime() - starttime.getTime();
        return (int) seconds / (1000 * 60);
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 保存现在的时间至最大天数天后的时间
     *
     * @return
     */
    public static List<String> getDateList(int maxDate) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat("dd/MM/yyyy");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < maxDate-1; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        date = sim.format(c.getTime());
        dates.add(date);
        return dates;
    }

    /**
     * 将时间转换为时间戳
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * @param s
     * @return
     * @throws ParseException
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /***
     *  获取指定日后 后 dayAddNum 天的 日期
     *  @param day  日期，格式为String："2013-9-3";
     *  @param dayAddNum 增加天数 格式为int;
     *  @return
     */
    public static String getDateStr(String day, long dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    /**
     * 获取年月日
     *
     * @return
     */
    public static List<Integer> getYearMonthDateList() {
        long timestamp;
        long curTime = System.currentTimeMillis()/1000;
        if(PreferenceUtil.getServiceDiffTime()>0){
            timestamp= PreferenceUtil.getServiceDiffTime()+curTime;
        } else {
            timestamp = curTime;
        }
        String times = stampToDate(timestamp*1000+"");
        String[] time = times.split("/");

        List<Integer> timesList = new ArrayList<>();//0年 1月 2日
        timesList.add(Integer.valueOf(time[2]));
        timesList.add(Integer.valueOf(time[1]));
        timesList.add(Integer.valueOf(time[0]));
        return timesList;
    }

    /**
     * 获取年月日
     *
     * @return
     */
    public static List<Integer> getTimeList() {
        List<Integer> timesList = new ArrayList<>();//0年 1月 2日
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);
        timesList.add(Integer.valueOf(year));
        timesList.add(Integer.valueOf(month));
        timesList.add(Integer.valueOf(day));
        return timesList;
    }

    /**
     * 获取指定日期
     * 返回格式20161117
     *
     * @param dateType
     * @author Yangtse
     */
    //使用方法 char datetype = '7';
    public static StringBuilder getSpecifiedDate(char dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case '0': // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // Log.v(df.format(c.getTime()));
                break;
            case '1': // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // Log.v(df.format(c.getTime()));
                break;
            case '2': // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // Log.v(df.format(c.getTime()));
                break;
            case '3': // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // Log.v(df.format(c.getTime()));
                break;
            case '4': // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // Log.v(df.format(c.getTime()));
                break;
            case '5': // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                // Log.v(df.format(c.getTime()));
                break;
            case '6': // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 6;
                c.set(Calendar.DAY_OF_MONTH, day);
                // Log.v(df.format(c.getTime()));
                break;
            case '7': // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                // Log.v(df.format(c.getTime()));
                break;
            case '8': // 三个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 90;
                c.set(Calendar.DAY_OF_MONTH, day);
                // Log.v(df.format(c.getTime()));
                break;
            case '9': //六个月后
                day = c.get(Calendar.DAY_OF_MONTH) + 180;
                c.set(Calendar.DAY_OF_MONTH, day);
                // Log.v(df.format(c.getTime()));
                break;


        }
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder strForwardDate = new StringBuilder().append(mYear).append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(
                (mDay < 10) ? "0" + mDay : mDay);
      //  Log.v("strDate------->" + strForwardDate + "-" + c.getTimeInMillis());
        return strForwardDate;
        //return c.getTimeInMillis();
    }
}
