package cn.edu.ahnu.wjcy.myapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/15 14:31，文件位于com.xiaodou.face.util
 * 文件作用：
 */

public class TimeUtils {
    /**
     * 获取当前的时间格式HH:mm:ss 24小时制度
     */
    private  static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");

    /**
     * 判断时间是否在允许时间范围之内
     * @return
     */
    public static boolean testTimeBetween(){
        //String nowTime = simpleDateFormat.format(Calendar.getInstance().getTime());
        //return isInTime("09:00:00-18:00:00",nowTime);
        return true;
    }
    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime
     *            时间区间,半闭合,如[10:00-20:00)
     * @param curTime
     *            需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            }
            else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }
}
