package com.nowcode.question_answer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;


@Service
public class JedisAdapt implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapt.class);
    private JedisPool pool;//连接池

    @Override
    //连接池初始化
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    //开启事务
    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }
        return null;
    }

    //执行事务
    public List<Object> exec(Transaction transaction,Jedis jedis){
        try {
            return transaction.exec();
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            if(transaction!=null){
                try{
                    transaction.close();
                } catch (IOException ioe) {
                    logger.error("发生异常："+ioe.getMessage());
                }
            }
            if(jedis!=null){
                try {
                    jedis.close();
                } catch (Exception e){
                    logger.error("发生异常："+e.getMessage());
                }
            }
        }
        return null;
    }

    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.zrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return null;
    }

    public long zadd(String key, double score, String member){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.zadd(key,score,member);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
    }

    public long zrem(String key, String member){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.zrem(key,member);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if(jedis!=null)
                jedis.close();
        }
        return 0;
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

    public long lpush(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout,key);
        } catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

}
