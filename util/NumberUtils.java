package com.threetree.baseproject.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by cks on 18-4-10.
 *
 *
 */

public class NumberUtils {

    /**
     * 手机号脱敏显示 （8位或11位）
     *
     **/
    public static String phoneNumHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        if (param.length() == 8){
            return param.replaceAll("(\\d{2})\\d{4}(\\d{2})","$1****$2");
        } else if (param.length() == 11) {
            return param.replaceAll("(\\d{4})\\d{3}(\\d{4})","$1***$2");
        } else if (param.length() == 14) {
            return param.replaceAll("(\\d{6})\\d{4}(\\d{4})","$1****$2");
        } else if (param.length() == 15) {
            return param.replaceAll("(\\d{7})\\d{4}(\\d{4})","$1****$2");
        }
        return null;
    }

    public static String subStringLast11(String param){
        if (TextUtils.isEmpty(param)) return null;
        //截取后11位数
        try {
            param = param.substring(param.length()-11,param.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    /**
     * 元数据先截取后11位数子
     * 手机号脱敏显示 （8位或11位）
     *
     **/
    public static String phoneNumHidden (String param, boolean needSubString){
        if (TextUtils.isEmpty(param)) return null;
        //截取后11位数
        try {
            param = param.substring(param.length()-11,param.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (param.length() == 8){
            return param.replaceAll("(\\d{2})\\d{4}(\\d{2})","$1****$2");
        } else if (param.length() == 11) {
            return param.replaceAll("(\\d{4})\\d{3}(\\d{4})","$1***$2");
        } else if (param.length() == 14) {
            return param.replaceAll("(\\d{6})\\d{4}(\\d{4})","$1****$2");
        } else if (param.length() == 15) {
            return param.replaceAll("(\\d{7})\\d{4}(\\d{4})","$1****$2");
        }
        return null;
    }

    /**
     * 身份证号脱敏显示 （18位）
     *
     **/
    public static String idCardNumHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        return param.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2");
    }

    /**
     * BVN脱敏显示 （18位）
     *
     **/
    public static String bvnNumHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        int length = param.length()-2;
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<length; i++){
            sb.append("*");
        }
        return param.replaceAll("(\\w{1})\\w{"+length+"}(\\w{1})","$1"+sb+"$2");
    }

    public static String bankcardHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        int length = param.length();
        return param.substring(length-4,length);
    }


    /**
     * 进位处理
     * @param param
     * @return
     */
    public static double decimalFormat (double param){
        BigDecimal bigDecima = new BigDecimal(new Double(param).toString());
        double total;
        total = bigDecima.setScale(2, BigDecimal.ROUND_UP).doubleValue();

        //小数点后第三位数为0时不进1
        try {
            String s = param + "";
            String[] split = s.split("\\.");
            s = split[split.length-1];
            if (s.length()>=3){
                if (s.charAt(2)=='0'){
                    total = bigDecima.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        DecimalFormat df = new DecimalFormat("###.00");
//        String format = df.format(total);
//        if (format.startsWith(".")){
//            format = "0" + format;
//        }
        return total;
    }

    /**
     * 保留2位小数且三位三位的隔开
     * 小数点后第三位数为0时不进1,否则进1
     *
     **/
    public static String moneyFormat(double param){
        BigDecimal bigDecima = new BigDecimal(new Double(param).toString());
        double total;
        total = bigDecima.setScale(2, BigDecimal.ROUND_UP).doubleValue();

        //小数点后第三位数为0时不进1
        try {
            String s = param + "";
            String[] split = s.split("\\.");
            s = split[split.length-1];
            if (s.length()>=3){
                if (s.charAt(2)=='0'){
                    total = bigDecima.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DecimalFormat df = new DecimalFormat("#,###.00");
        String format = df.format(total);
        if (format.startsWith(".")){
            format = "0" + format;
        }
        return format;
    }


    /**
     *
     *
     **/
    public static String decimalKFormat(double param){
        BigDecimal bigDecima = new BigDecimal(new Double(param).toString());
        double total;
        total = bigDecima.setScale(2, BigDecimal.ROUND_UP).doubleValue();

        //小数点后第三位数为0时不进1
        try {
            String s = param + "";
            String[] split = s.split("\\.");
            s = split[split.length-1];
            if (s.length()>=3){
                if (s.charAt(2)=='0'){
                    total = bigDecima.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DecimalFormat df = new DecimalFormat("#,###");
        String format = df.format(total);
        if (format.startsWith(".")){
            format = "0" + format;
        }
        return format;
    }
}
