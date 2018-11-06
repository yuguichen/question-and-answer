package com.nowcode.question_answer.service;

import com.nowcode.question_answer.utils.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.Element;

/**
 * 赞和踩
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapt jedisAdapt;

    public long like(int userId,int entityType,int entityId){
        String likeKey = JedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapt.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = JedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapt.srem(disLikeKey,String.valueOf(userId));//移除踩，避免同时赞和踩

        return jedisAdapt.scard(likeKey);
    }

    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey = JedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapt.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = JedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapt.srem(likeKey,String.valueOf(userId));

        return jedisAdapt.scard(likeKey);
    }

    //用户对当前评论状态:1：赞  -1:踩  0：无状态
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = JedisKeyUtil.getLikeKey(entityType,entityId);
        String disLikeKey = JedisKeyUtil.getDisLikeKey(entityType,entityId);

        return jedisAdapt.sismember(likeKey,String.valueOf(userId))?1:(jedisAdapt.sismember(disLikeKey,String.valueOf(userId))?-1:0);
    }

    //赞的人数
    public long getLikeCount(int entityType,int entityId){
        return jedisAdapt.scard(JedisKeyUtil.getLikeKey(entityType,entityId));
    }

}
