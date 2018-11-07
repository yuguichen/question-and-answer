package com.nowcode.question_answer.async;

import com.alibaba.fastjson.JSON;
import com.nowcode.question_answer.service.JedisAdapt;
import com.nowcode.question_answer.utils.JedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;
    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    @Autowired
    JedisAdapt jedisAdapt;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null){
            //遍历所有Handler的实现类，并得到相关事件类型，
            // 以事件类型为key值，将与类型相关联的实现类存入value中，
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for(EventType eventType:eventTypes){
                    if(!config.containsKey(eventType))
                        config.put(eventType,new ArrayList<EventHandler>());
                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        //等待事件进入队列后进行处理
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = JedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapt.brpop(0,key);

                    for(String event:events){
                        if(event.equals(key))
                            continue;
                        EventModel eventModel = JSON.parseObject(event, EventModel.class);
                        if(!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别该事件");
                            continue;
                        }

                        for(EventHandler handler:config.get(eventModel.getEventType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
