package com.nowcode.question_answer.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcode.question_answer.async.EventHandler;
import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.Feed;
import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.service.*;
import com.nowcode.question_answer.utils.JedisKeyUtil;
import com.nowcode.question_answer.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapt jedisAdapt;

    private String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if(actor ==null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getUsername());

        if(model.getEventType() == EventType.COMMENT ||
                (model.getEventType()==EventType.FOLLOW && model.getEntityType() == MyUtils.ENTITY_QUESTION)){
            Question question = questionService.selectByID(model.getEntityId());
            if(question==null)
                return null;
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
        }
        return JSONObject.toJSONString(map);
    }

    @Override
    public void doHandle(EventModel model) {
        //新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getEventType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if(feed.getData()==null)
            return;
        feedService.addFeed(feed);


        //推模式
        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(MyUtils.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = JedisKeyUtil.getTimelineKey(follower);
            jedisAdapt.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
