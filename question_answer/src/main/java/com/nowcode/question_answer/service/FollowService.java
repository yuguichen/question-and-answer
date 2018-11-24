package com.nowcode.question_answer.service;

import com.nowcode.question_answer.utils.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    JedisAdapt jedisAdapt;

    //关注
    public boolean follow(int userId,int entityType,int entityId){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapt.getJedis();
        Transaction transaction = jedisAdapt.multi(jedis);
        transaction.zadd(followerKey,date.getTime(),String.valueOf(userId));
        transaction.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapt.exec(transaction, jedis);
        return ret.size()==2 && (long)ret.get(0)>0 && (long)ret.get(1)>0;
    }

    //取消关注
    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        Jedis jedis = jedisAdapt.getJedis();
        Transaction transaction = jedisAdapt.multi(jedis);
        transaction.zrem(followerKey,String.valueOf(userId));
        transaction.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapt.exec(transaction, jedis);
        return ret.size()==2 && (long)ret.get(0)>0 && (long)ret.get(1)>0;
    }

    /**
     * 获取粉丝列表
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        List<Integer> ids = new ArrayList<>();
        for(String id:jedisAdapt.zrevrange(followerKey,0,count)){
            ids.add(Integer.valueOf(id));
        }
        return ids;
    }

    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        List<Integer> ids = new ArrayList<>();
        for(String id:jedisAdapt.zrevrange(followerKey,offset,count)){
            ids.add(Integer.valueOf(id));
        }
        return ids;
    }

    public long getFollowerCount(int entityType,int entityId){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapt.zcard(followerKey);
    }

    /**
     * 获取所有关注事件
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId,int entityType,int count){
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        List<Integer> ids = new ArrayList<>();
        for(String id:jedisAdapt.zrevrange(followeeKey,0,count)){
            ids.add(Integer.valueOf(id));
        }
        return ids;
    }

    public List<Integer> getFollowees(int userId,int entityType,int offset,int count){
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        List<Integer> ids = new ArrayList<>();
        for(String id:jedisAdapt.zrevrange(followeeKey,offset,count)){
            ids.add(Integer.valueOf(id));
        }
        return ids;
    }

    public long getFolloweeCount(int userId,int entityType){
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdapt.zcard(followeeKey);
    }

    public boolean isFollower(int userId,int entityType,int entityId ){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapt.zscore(followerKey,String.valueOf(userId))!=null;
    }

}
