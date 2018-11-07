package com.nowcode.question_answer.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcode.question_answer.service.JedisAdapt;
import com.nowcode.question_answer.utils.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapt jedisAdapt;

    /**
     * 将 事件发送到 队列中，等待处理
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = JedisKeyUtil.getEventQueueKey();
            jedisAdapt.lpush(key,json);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}
