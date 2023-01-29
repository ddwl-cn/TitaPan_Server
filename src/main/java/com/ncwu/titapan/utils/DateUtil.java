package com.ncwu.titapan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/6 8:58
 */
public class DateUtil {

    /**
     * TODO 返回格式化日期 例如：2023-01-06,08:07:59
     *
     * @return lang.String
     * @Author ddwl.
     * @Date 2023/1/6 8:58
    **/
    public static String getFormatDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    public static boolean biggerThan(String formatDate_1, String formatDate_2){
        if(Integer.parseInt(DateUtil.getY(formatDate_1))>Integer.parseInt(DateUtil.getY(formatDate_2))){
            return true;
        }
        else if(Integer.parseInt(DateUtil.getY(formatDate_1))<Integer.parseInt(DateUtil.getY(formatDate_2)))
            return false;

        if(Integer.parseInt(DateUtil.getM(formatDate_1))>Integer.parseInt(DateUtil.getM(formatDate_2))){
            return true;
        }
        else if(Integer.parseInt(DateUtil.getM(formatDate_1))<Integer.parseInt(DateUtil.getM(formatDate_2)))
            return false;
        if(Integer.parseInt(DateUtil.getD(formatDate_1))>Integer.parseInt(DateUtil.getD(formatDate_2))){
            return true;
        }
        else if(Integer.parseInt(DateUtil.getD(formatDate_1))<Integer.parseInt(DateUtil.getD(formatDate_2)))
            return false;
        if(Integer.parseInt(DateUtil.getH(formatDate_1))>Integer.parseInt(DateUtil.getH(formatDate_2))){
            return true;
        }
        else if(Integer.parseInt(DateUtil.getH(formatDate_1))<Integer.parseInt(DateUtil.getH(formatDate_2)))
            return false;

        if(Integer.parseInt(DateUtil.getMi(formatDate_1))>Integer.parseInt(DateUtil.getMi(formatDate_2))){
            return true;
        }
        return false;
    }


    public static String getY(String formatDate){
        return formatDate.substring(0, 4);
    }
    public static String getM(String formatDate){
        return formatDate.substring(5, 7);
    }
    public static String getD(String formatDate){
        return formatDate.substring(8, 10);
    }
    public static String getH(String formatDate){
        return formatDate.substring(11, 13);
    }
    public static String getMi(String formatDate){
        return formatDate.substring(14, 16);
    }

    public static String getYMD(String formatDate){
        return formatDate.substring(0, 10);
    }

    public static String getHMS(String formatDate){
        return formatDate.substring(11);
    }

    public static void main(String []args){
        System.out.println(getFormatDate());
        System.out.println(getYMD(getFormatDate()));
        System.out.println(getHMS(getFormatDate()));
    }


}
