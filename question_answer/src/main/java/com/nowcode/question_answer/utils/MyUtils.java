package com.nowcode.question_answer.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyUtils {
    private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    //评论的实体：问题或评论
    public static int ENTITY_QUESTION = 1;
    public static int ENTITY_COMMENT = 2;
    public static int ENTITY_USER = 3;

    public static int ANONYMOUS_USER_ID = 3;  //匿名用户id
    public static int SYSTEM_USERID = 4;

    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}
