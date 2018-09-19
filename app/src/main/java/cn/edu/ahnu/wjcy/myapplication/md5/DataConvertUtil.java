package cn.edu.ahnu.wjcy.myapplication.md5;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Ringo on 2016/8/3.
 * 数据转换辅助类
 */
public class DataConvertUtil {

    /**
     * 将JSONArray对象转换成list集合
     *
     * @param jsonArr
     * @return
     */
    public static List<Object> jsonToList(JSONArray jsonArr) {
        List<Object> list = new ArrayList<Object>();
        for (Object obj : jsonArr) {
            if (obj instanceof JSONArray) {
                list.add(jsonToList((JSONArray) obj));
            } else if (obj instanceof JSONObject) {
                list.add(jsonToMap((JSONObject) obj));
            } else {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 将JSONObject转换成map对象
     *
     * @param jsonObj
     * @return
     */
    public static SortedMap<String, Object> jsonToMap(JSONObject jsonObj) throws JSONException {
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        for (String key : jsonObj.keySet()) {
            Object value = jsonObj.get(key);
            if (value instanceof JSONArray) {
                map.put(key, jsonToList((JSONArray) value));
            } else if (value instanceof JSONObject) {
                map.put(key, jsonToMap((JSONObject) value));
            } else {
                map.put(key, jsonObj.get(key));
            }
        }
        return map;
    }
}
