package top.alexmmd.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 汪永晖
 */
public class RandomTools {

    /** 随机6位数 **/
    public static String randomCode(){
        Integer res=(int)((Math.random())*1000000);
        return res+"";
    }
    /*时间捕捉*/
    public static String curDate(String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }
    public static String curDate(){
        return curDate("yyyy-MM-dd HH:mm:ss");
    }
}
