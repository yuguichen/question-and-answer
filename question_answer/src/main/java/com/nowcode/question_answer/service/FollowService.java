package com.nowcode.question_answer.service;

import com.nowcode.question_answer.utils.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

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
    public boolean unFollow(int userId,int entityType,int entityId){
        String followerKey = JedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = JedisKeyUtil.getFolloweeKey(userId,entityType);
        Jedis jedis = jedisAdapt.getJedis();
        Transaction transaction = jedisAdapt.multi(jedis);
        transaction.zrem(followerKey,String.valueOf(userId));
        transaction.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapt.exec(transaction, jedis);
        return ret.size()==2 && (long)ret.get(0)>0 && (long)ret.get(1)>0;
    }

}
