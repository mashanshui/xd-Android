/**
 * <p>Title: MD5.java</p>
 * <p>MD5</p>
 *
 * @author damon
 * @date 2015年3月24日
 * @version 1.0
 */
package cn.edu.ahnu.wjcy.myapplication.md5;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>Title: MD5</p>
 * <p>MD5</p> 
 * @author damon
 * @date 2015年3月24日
 */
public class MD5 {
    public static final String KEY = "testJIZHIsignKey";

    public static String getMd5Key(HashMap<String, Object> hashMap) throws JSONException {
        SortedMap<String, Object> sortedMap;
        sortedMap = DataConvertUtil.jsonToMap(new JSONObject(hashMap));
        String key = MD5.MD5sign(sortedMap, KEY);
        return key;

    }

    public static String Md532(String plainText, String charSetName) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer("");
        try {
            md = MessageDigest.getInstance("MD5");
            if (isBank(charSetName))
                md.update(plainText.getBytes());
            else
                md.update(plainText.getBytes(charSetName));

            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String Md532(String plainText) {
        return Md532(plainText, "utf-8");
    }

    public static String Md516(String plainText) {
        return Md532(plainText).substring(8, 24);
    }

    /**
     * MD5生成签名字符串
     *
     * @param paramsMap
     * @param keyStr
     * @return
     */
    public static String MD5sign(Map<String, Object> paramsMap, String keyStr) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)) {
                stringBuffer.append(k + "=" + v + "&");
            }
        }
        stringBuffer.append("key=").append(keyStr);
//		System.out.println(stringBuffer.toString());
        String sign = MD5.Md532(stringBuffer.toString()).toUpperCase();
        return sign;
    }

    /**
     * MD5验签， map中包含了 “sign”的时候用这个方法
     * @param map
     * @param key
     * @return
     */
    public static boolean vlidateMD5sign(Map<String, Object> map, String key) {
        String sign = map.get("sign") + "";
        map.remove("sign");
        return vlidateMD5sign(map, key, sign);
    }

    /**
     * MD5验签
     * @param map
     * @param key
     * @param sign
     * @return
     */
    public static boolean vlidateMD5sign(Map<String, Object> map, String key, String sign) {
        String vsign = MD5sign(map, key);
        if (vsign.equals(sign))
            return true;
        return false;
    }

    public static boolean isBank(String context) {
        if (context == null || context.equals("") || context.trim().equals("")) {
            return true;
        }
        return false;
    }

}

