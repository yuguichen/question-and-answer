package com.nowcode.question_answer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class JedisAdapt implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapt.class);
    private JedisPool pool;//连接池

    @Override
    //连接池初始化
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    //add
    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    //remove
    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    //get the number of members in a set
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    //判断元素是否存在
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return false;
    }


}
